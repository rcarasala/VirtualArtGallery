package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.model.Art;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class ViewArtServlet extends HttpServlet {

    // art service for database operations and logics
    ArtService artService = new ArtServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Check if the customer is logged in, or else return to login page
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {

            // Read All available arts from the database
            List<Art> arts = artService.getAllArts();

            // Default Page to load data into
            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            // Set Available Art tab as active
            StoreUtil.setActiveTab(pw, "arts");

            // Show the heading for the page
            pw.println("<div id='topmid' style='background-color:grey'>Available Art"
                    + "<form action=\"cart\" method=\"post\" style='float:right; margin-right:20px'>"
                    + "<input type='submit' class=\"btn btn-primary\" name='cart' value='Proceed'/></form>"
                    + "</div>");
            pw.println("<div class=\"container\">\r\n"
                    + "        <div class=\"card-columns\">");

            // Add or Remove items from the cart, if requested
            StoreUtil.updateCartItems(req);

            HttpSession session = req.getSession();
            for (Art art : arts) {

                // Add each art to display as a card
                pw.println(this.addArtToCard(session, art));

            }

            // Checkout Button
            pw.println("</div>"
                    + "<div style='float:auto'><form action=\"cart\" method=\"post\">"
                    + "<input type='submit' class=\"btn btn-success\" name='cart' value='Proceed to Checkout'/></form>"
                    + "    </div>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addArtToCard(HttpSession session, Art art) {
        String bCode = art.getBarcode();
        int bQty = art.getQuantity();

        // Quantity of the current art added to the cart
        int cartItemQty = 0;
        if (session.getAttribute("qty_" + bCode) != null) {
            // Quantity of each art in the cart will be added in the session prefixed with
            // 'qty_' following with artId
            cartItemQty = (int) session.getAttribute("qty_" + bCode);
        }

        // Button To Add/Remove item from the cart
        String button = "";
        if (bQty > 0) {
            // If no items in the cart, show add to cart button
            // If items is added to the cart, then show +, - button to add/remove more items
            button = "<form action=\"viewart\" method=\"post\">"
                    + "<input type='hidden' name = 'selectedArtId' value = " + bCode + ">"
                    + "<input type='hidden' name='qty_" + bCode + "' value='1'/>"
                    + (cartItemQty == 0
                            ? "<input type='submit' class=\"btn btn-primary\" name='addToCart' value='Add To Cart'/></form>"
                            : "<form method='post' action='cart'>"
                                    + "<button type='submit' name='removeFromCart' class=\"glyphicon glyphicon-minus btn btn-danger\"></button> "
                                    + "<input type='hidden' name='selectedArtId' value='" + bCode + "'/>"
                                    + cartItemQty
                                    + " <button type='submit' name='addToCart' class=\"glyphicon glyphicon-plus btn btn-success\"></button></form>")
                    + "";
        } else {
            // If available Quantity is zero, show out of stock button
            button = "<p class=\"btn btn-danger\">Out Of Stock</p>\r\n";
        }

        // Bootstrap card to show the art data
        return "<div class=\"card\">\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <img class=\"col-sm-6\" src=\"logo.png\" alt=\"Card image cap\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <h5 class=\"card-title text-success\">" + art.getName() + "</h5>\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Artist: <span class=\"text-primary\" style=\"font-weight:bold;\"> "
                + art.getArtist()
                + "</span><br>\r\n"
                + "                        </p>\r\n"
                + "                        \r\n"
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        <span>Id: " + bCode + "</span>\r\n"
                + (bQty < 20 ? "<br><span class=\"text-danger\">Only " + bQty + " items left</span>\r\n"
                        : "<br><span class=\"text-success\">Trending</span>\r\n")
                + "                        </p>\r\n"
                + "                    </div>\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Price: <span style=\"font-weight:bold; color:green\"> &#8377; "
                + art.getPrice()
                + " </span>\r\n"
                + "                        </p>\r\n"
                + button
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "            </div>";
    }
}
