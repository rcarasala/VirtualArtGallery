package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ArtStoreConstants;
import com.bittercode.model.Art;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class BuyArtsServlet extends HttpServlet {
    ArtService artService = new ArtServiceImpl();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(ArtStoreConstants.CONTENT_TYPE_TEXT_HTML);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            List<Art> arts = artService.getAllArts();
            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "cart");
            pw.println("<div class=\"tab hd brown \">Arts Available In Our Store</div>");
            pw.println("<div class=\"tab\"><form action=\"buys\" method=\"post\">");
            pw.println("<table>\r\n" +
                    "			<tr>\r\n" +
                    "				<th>Arts</th>\r\n" +
                    "				<th>Code</th>\r\n" +
                    "				<th>Name</th>\r\n" +
                    "				<th>Artist</th>\r\n" +
                    "				<th>Price</th>\r\n" +
                    "				<th>Avail</th>\r\n" +
                    "				<th>Qty</th>\r\n" +
                    "			</tr>");
            int i = 0;
            for (Art art : arts) {
                String bCode = art.getBarcode();
                String bName = art.getName();
                String bArtist = art.getArtist();
                double bPrice = art.getPrice();
                int bAvl = art.getQuantity();
                i = i + 1;
                String n = "checked" + Integer.toString(i);
                String q = "qty" + Integer.toString(i);
                pw.println("<tr>\r\n" +
                        "				<td>\r\n" +
                        "					<input type=\"checkbox\" name=" + n + " value=\"pay\">\r\n" + // Value is
                                                                                                          // made equal
                                                                                                          // to bcode
                        "				</td>");
                pw.println("<td>" + bCode + "</td>");
                pw.println("<td>" + bName + "</td>");
                pw.println("<td>" + bArtist + "</td>");
                pw.println("<td>" + bPrice + "</td>");
                pw.println("<td>" + bAvl + "</td>");
                pw.println("<td><input type=\"text\" name=" + q + " value=\"0\" text-align=\"center\"></td></tr>");

            }
            pw.println("</table>\r\n" + "<input type=\"submit\" value=\" PAY NOW \">" + "<br/>" +
                    "	</form>\r\n" +
                    "	</div>");
            // pw.println("<div class=\"tab\"><a href=\"Addart.html\">Add More
            // arts</a></div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
