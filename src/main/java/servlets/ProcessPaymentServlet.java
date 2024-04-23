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

import com.bittercode.constant.ArtStoreConstants;
import com.bittercode.model.Art;
import com.bittercode.model.Cart;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class ProcessPaymentServlet extends HttpServlet {

    ArtService artService = new ArtServiceImpl();

    @SuppressWarnings("unchecked")
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(ArtStoreConstants.CONTENT_TYPE_TEXT_HTML);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {

            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "cart");
            pw.println("<div id='topmid' style='background-color:grey'>Your Orders</div>");
            pw.println("<div class=\"container\">\r\n"
                    + "        <div class=\"card-columns\">");
            HttpSession session = req.getSession();
            List<Cart> cartItems = null;
            if (session.getAttribute("cartItems") != null)
                cartItems = (List<Cart>) session.getAttribute("cartItems");
            for (Cart cart : cartItems) {
                Art art = cart.getArt();
                double bPrice = art.getPrice();
                String bCode = art.getBarcode();
                String bName = art.getName();
                String bArtist = art.getArtist();
                int availableQty = art.getQuantity();
                int qtToBuy = cart.getQuantity();
                availableQty = availableQty - qtToBuy;
                artService.updateArtQtyById(bCode, availableQty);
                pw.println(this.addArtToCard(bCode, bName, bArtist, bPrice, availableQty));
                session.removeAttribute("qty_" + bCode);
            }
            session.removeAttribute("amountToPay");
            session.removeAttribute("cartItems");
            session.removeAttribute("items");
            session.removeAttribute("selectedArtId");
            pw.println("</div>\r\n"
                    + "    </div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addArtToCard(String bCode, String bName, String bArtist, double bPrice, int bQty) {
        String button = "<a href=\"#\" class=\"btn btn-info\">Order Placed</a>\r\n";
        return "<div class=\"card\">\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <img class=\"col-sm-6\" src=\"logo.png\" alt=\"Card image cap\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <h5 class=\"card-title text-success\">" + bName + "</h5>\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Artist: <span class=\"text-primary\" style=\"font-weight:bold;\"> " + bArtist
                + "</span><br>\r\n"
                + "                        </p>\r\n"
                + "                        \r\n"
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        <span style='color:blue;'>Order Id: ORD" + bCode + "TM </span>\r\n"
                + "                        <br><span class=\"text-danger\">Item Yet to be Delivered</span>\r\n"
                + "                        </p>\r\n"
                + "                    </div>\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Amout Paid: <span style=\"font-weight:bold; color:green\"> &#8377; " + bPrice
                + " </span>\r\n"
                + "                        </p>\r\n"
                + button
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "            </div>";
    }
}
