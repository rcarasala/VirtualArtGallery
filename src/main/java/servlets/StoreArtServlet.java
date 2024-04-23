package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.model.Art;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class StoreArtServlet extends HttpServlet {

    // art service for database operations and logics
    ArtService artService = new ArtServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Check if the customer is logged in, or else return to login page
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {

            // Add/Remove Item from the cart if requested
            // store the comma separated Art of cart in the session
            // StoreUtil.updateCartItems(req);

            RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
            rd.include(req, res);
            pw.println("<div class='container'>");
            // Set the active tab as cart
            StoreUtil.setActiveTab(pw, "storearts");

            // Read the arts from the database with the respective artIds
            List<Art> arts = artService.getAllArts();
            pw.println("<div id='topmid' style='background-color:grey'>Art Available In the Store</div>");
            pw.println("<table class=\"table table-hover\" style='background-color:white'>\r\n"
                    + "  <thead>\r\n"
                    + "    <tr style='background-color:black; color:white;'>\r\n"
                    + "      <th scope=\"col\">ArtId</th>\r\n"
                    + "      <th scope=\"col\">Name</th>\r\n"
                    + "      <th scope=\"col\">Artist</th>\r\n"
                    + "      <th scope=\"col\">Price</th>\r\n"
                    + "      <th scope=\"col\">Quantity</th>\r\n"
                    + "      <th scope=\"col\">Action</th>\r\n"
                    + "    </tr>\r\n"
                    + "  </thead>\r\n"
                    + "  <tbody>\r\n");
            if (arts == null || arts.size() == 0) {
                pw.println("    <tr style='background-color:green'>\r\n"
                        + "      <th scope=\"row\" colspan='6' style='color:yellow; text-align:center;'> No arts Available in the store </th>\r\n"
                        + "    </tr>\r\n");
            }
            for (Art art : arts) {
                pw.println(getRowData(art));
            }

            pw.println("  </tbody>\r\n"
                    + "</table></div>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRowData(Art art) {
        return "    <tr>\r\n"
                + "      <th scope=\"row\">" + art.getBarcode() + "</th>\r\n"
                + "      <td>" + art.getName() + "</td>\r\n"
                + "      <td>" + art.getArtist() + "</td>\r\n"
                + "      <td><span>&#8377;</span> " + art.getPrice() + "</td>\r\n"
                + "      <td>"
                + art.getQuantity()
                + "      </td>\r\n"
                + "      <td><form method='post' action='updateart'>"
                + "          <input type='hidden' name='artId' value='" + art.getBarcode() + "'/>"
                + "          <button type='submit' class=\"btn btn-success\">Update</button>"
                + "          </form>"
                + "    </tr>\r\n";
    }

}
