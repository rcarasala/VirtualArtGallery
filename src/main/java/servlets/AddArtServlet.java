package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ArtStoreConstants;
import com.bittercode.constant.db.ArtsDBConstants;
import com.bittercode.model.Art;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class AddArtServlet extends HttpServlet {
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

        String bName = req.getParameter(ArtsDBConstants.COLUMN_NAME);
        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "addart");
        pw.println("<div class='container my-2'>");
        if(bName == null || bName.isBlank()) {
            //render the add art form;
            showAddArtForm(pw);
            return;
        } //else process the add art
        
 
        try {
            String uniqueID = UUID.randomUUID().toString();
            String bCode = uniqueID;
            String bArtist = req.getParameter(ArtsDBConstants.COLUMN_ARTIST);
            double bPrice = Integer.parseInt(req.getParameter(ArtsDBConstants.COLUMN_PRICE));
            int bQty = Integer.parseInt(req.getParameter(ArtsDBConstants.COLUMN_QUANTITY));

            Art art = new Art(bCode, bName, bArtist, bPrice, bQty);
            String message = artService.addArt(art);
            if ("SUCCESS".equalsIgnoreCase(message)) {
                pw.println(
                        "<table class=\"tab\"><tr><td>Art Detail Updated Successfully!<br/>Add More Arts</td></tr></table>");
            } else {
                pw.println("<table class=\"tab\"><tr><td>Failed to Add Arts! Fill up CareFully</td></tr></table>");
                //rd.include(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Add Arts! Fill up CareFully</td></tr></table>");
        }
    }
    
    private static void showAddArtForm(PrintWriter pw) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"addart\" method=\"post\">\r\n"
                + "                    <!-- <label for=\"artCode\">Art Code : </label><input type=\"text\" name=\"barcode\" id=\"artCode\" placeholder=\"Enter art Code\" required><br/> -->\r\n"
                + "                    <label for=\"artName\">Art Name : </label> <input type=\"text\" name=\"name\" id=\"artName\" placeholder=\"Enter art's name\" required><br/>\r\n"
                + "                    <label for=\"artArtist\">Art Artist : </label><input type=\"text\" name=\"artist\" id=\"artArtist\" placeholder=\"Enter Artist's Name\" required><br/>\r\n"
                + "                    <label for=\"artPrice\">Art Price : </label><input type=\"number\" name=\"price\" placeholder=\"Enter the Price\" required><br/>\r\n"
                + "                    <label for=\"artQuantity\">Art Qnty : </label><input type=\"number\" name=\"quantity\" id=\"artQuantity\" placeholder=\"Enter the quantity\" required><br/>\r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" value=\" Add Art \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "        <!-- <tr>\r\n"
                + "            <td><a href=\"index.html\">Go Back To Home Page</a></td>\r\n"
                + "        </tr> -->\r\n"
                + "    </table>";
        pw.println(form);
    }
}
