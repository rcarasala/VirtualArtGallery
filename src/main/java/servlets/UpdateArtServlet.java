package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ArtStoreConstants;
import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.ArtsDBConstants;
import com.bittercode.model.Art;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class UpdateArtServlet extends HttpServlet {
    ArtService artService = new ArtServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(ArtStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "storearts");
        pw.println("<div class='container my-2'>");

        try {
            if (req.getParameter("updateFormSubmitted") != null) {
                String bName = req.getParameter(ArtsDBConstants.COLUMN_NAME);
                String bCode = req.getParameter(ArtsDBConstants.COLUMN_BARCODE);
                String bArtist = req.getParameter(ArtsDBConstants.COLUMN_ARTIST);
                double bPrice = Double.parseDouble(req.getParameter(ArtsDBConstants.COLUMN_PRICE));
                int bQty = Integer.parseInt(req.getParameter(ArtsDBConstants.COLUMN_QUANTITY));

                Art art = new Art(bCode, bName, bArtist, bPrice, bQty);
                String message = artService.updateArt(art);
                if (ResponseCode.SUCCESS.name().equalsIgnoreCase(message)) {
                    pw.println(
                            "<table class=\"tab\"><tr><td>Art Detail Updated Successfully!</td></tr></table>");
                } else {
                    pw.println("<table class=\"tab\"><tr><td>Failed to Update Art!!</td></tr></table>");
                    // rd.include(req, res);
                }

                return;
            }

            String artId = req.getParameter("artId");

            if (artId != null) {
                Art art = artService.getArtById(artId);
                showUpdateArtForm(pw, art);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Load Art data!!</td></tr></table>");
        }
    }

    private static void showUpdateArtForm(PrintWriter pw, Art art) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"updateart\" method=\"post\">\r\n"
                + "                    <label for=\"artCode\">Art Code : </label><input type=\"text\" name=\"barcode\" id=\"artCode\" placeholder=\"Enter art Code\" value='"
                + art.getBarcode() + "' readonly><br/>"
                + "                    <label for=\"artName\">Art Name : </label> <input type=\"text\" name=\"name\" id=\"artName\" placeholder=\"Enter art's name\" value='"
                + art.getName() + "' required><br/>\r\n"
                + "                    <label for=\"artArtist\">Art Artist : </label><input type=\"text\" name=\"artist\" id=\"artArtist\" placeholder=\"Enter Artist's Name\" value='"
                + art.getArtist() + "' required><br/>\r\n"
                + "                    <label for=\"ArtPrice\">Art Price : </label><input type=\"number\" name=\"price\" placeholder=\"Enter the Price\" value='"
                + art.getPrice() + "' required><br/>\r\n"
                + "                    <label for=\"ArtQuantity\">Art Qnty : </label><input type=\"number\" name=\"quantity\" id=\"artQuantity\" placeholder=\"Enter the quantity\" value='"
                + art.getQuantity() + "' required><br/>\r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" name='updateFormSubmitted' value=\" Update art \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "    </table>";
        pw.println(form);
    }
}
