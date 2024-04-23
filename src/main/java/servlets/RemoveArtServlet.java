package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ResponseCode;
import com.bittercode.model.UserRole;
import com.bittercode.service.ArtService;
import com.bittercode.service.impl.ArtServiceImpl;
import com.bittercode.util.StoreUtil;

public class RemoveArtServlet extends HttpServlet {

    ArtService artService = new ArtServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        try {
            String artId = req.getParameter("artId");
            RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "removeart");
            pw.println("<div class='container'>");
            if (artId == null || artId.isBlank()) {
                // render the remove art form;
                showRemoveArtForm(pw);
                return;
            } // else continue

            String responseCode = artService.deleteArtById(artId.trim());
            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(responseCode)) {
                pw.println("<table class=\"tab my-5\"><tr><td>Art Removed Successfully</td></tr></table>");
                pw.println(
                        "<table class=\"tab\"><tr><td><a href=\"removeart\">Remove more Art</a></td></tr></table>");

            } else {
                pw.println("<table class=\"tab my-5\"><tr><td>Art Not Available In The Store</td></tr></table>");
                pw.println(
                        "<table class=\"tab\"><tr><td><a href=\"removeart\">Remove more Art</a></td></tr></table>");
            }
            pw.println("</div>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Remove Art! Try Again</td></tr></table>");
        }
    }

    private static void showRemoveArtForm(PrintWriter pw) {
        String form = "<form action=\"removeart\" method=\"post\" class='my-5'>\r\n"
                + "        <table class=\"tab\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <label for=\"artCode\">Enter ArtId to Remove </label>\r\n"
                + "                <input type=\"text\" name=\"artId\" placeholder=\"Enter Art Id\" id=\"artCode\" required>\r\n"
                + "                <input class=\"btn btn-danger my-2\" type=\"submit\" value=\"Remove Art\">\r\n"
                + "            </td>\r\n"
                + "        </tr>\r\n"
                + "\r\n"
                + "        </table>\r\n"
                + "    </form>";
        pw.println(form);
    }

}
