package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.ArtStoreConstants;
import com.bittercode.model.Art;
import com.bittercode.model.Cart;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class CartServlet extends HttpServlet {

    ArtService artService = new ArtServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(ArtStoreConstants.CONTENT_TYPE_TEXT_HTML);

        // Check if Customer is logged In
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            // Add/Remove Item from the cart if requested
            // store the comma separated artIds of cart in the session
            StoreUtil.updateCartItems(req);

            HttpSession session = req.getSession();
            String artIds = "";
            if (session.getAttribute("items") != null)
                artIds = (String) session.getAttribute("items");// read comma separated artIds from session

            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            // Set the active tab as cart
            StoreUtil.setActiveTab(pw, "cart");

            // Read the arts from the database with the respective artIds
            List<Art> arts = artService.getArtsByCommaSeperatedArtIds(artIds);
            List<Cart> cartItems = new ArrayList<Cart>();
            pw.println("<div id='topmid' style='background-color:grey'>Shopping Cart</div>");
            pw.println("<table class=\"table table-hover\" style='background-color:white'>\r\n"
                    + "  <thead>\r\n"
                    + "    <tr style='background-color:black; color:white;'>\r\n"
                    + "      <th scope=\"col\">ArtId</th>\r\n"
                    + "      <th scope=\"col\">Name</th>\r\n"
                    + "      <th scope=\"col\">Artist</th>\r\n"
                    + "      <th scope=\"col\">Price/Item</th>\r\n"
                    + "      <th scope=\"col\">Quantity</th>\r\n"
                    + "      <th scope=\"col\">Amount</th>\r\n"
                    + "    </tr>\r\n"
                    + "  </thead>\r\n"
                    + "  <tbody>\r\n");
            double amountToPay = 0;
            if (arts == null || arts.size() == 0) {
                pw.println("    <tr style='background-color:green'>\r\n"
                        + "      <th scope=\"row\" colspan='6' style='color:yellow; text-align:center;'> No Items In the Cart </th>\r\n"
                        + "    </tr>\r\n");
            }
            for (Art art : arts) {
                int qty = (int) session.getAttribute("qty_" + art.getBarcode());
                Cart cart = new Cart(art, qty);
                cartItems.add(cart);
                amountToPay += (qty * art.getPrice());
                pw.println(getRowData(cart));
            }

            // set cartItems and amountToPay in the session
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("amountToPay", amountToPay);

            if (amountToPay > 0) {
                pw.println("    <tr style='background-color:green'>\r\n"
                        + "      <th scope=\"row\" colspan='5' style='color:yellow; text-align:center;'> Total Amount To Pay </th>\r\n"
                        + "      <td colspan='1' style='color:white; font-weight:bold'><span>&#8377;</span> "
                        + amountToPay
                        + "</td>\r\n"
                        + "    </tr>\r\n");
            }
            pw.println("  </tbody>\r\n"
                    + "</table>");
            if (amountToPay > 0) {
                pw.println("<div style='text-align:right; margin-right:20px;'>\r\n"
                        + "<form action=\"checkout\" method=\"post\">"
                        + "<input type='submit' class=\"btn btn-primary\" name='pay' value='Proceed to Pay &#8377; "
                        + amountToPay + "'/></form>"
                        + "    </div>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRowData(Cart cart) {
        Art art = cart.getArt();
        return "    <tr>\r\n"
                + "      <th scope=\"row\">" + art.getBarcode() + "</th>\r\n"
                + "      <td>" + art.getName() + "</td>\r\n"
                + "      <td>" + art.getArtist() + "</td>\r\n"
                + "      <td><span>&#8377;</span> " + art.getPrice() + "</td>\r\n"
                + "      <td><form method='post' action='cart'><button type='submit' name='removeFromCart' class=\"glyphicon glyphicon-minus btn btn-danger\"></button> "
                + "<input type='hidden' name='selectedArtId' value='" + art.getBarcode() + "'/>"
                + cart.getQuantity()
                + " <button type='submit' name='addToCart' class=\"glyphicon glyphicon-plus btn btn-success\"></button></form></td>\r\n"
                + "      <td><span>&#8377;</span> " + (art.getPrice() * cart.getQuantity()) + "</td>\r\n"
                + "    </tr>\r\n";
    }

}
