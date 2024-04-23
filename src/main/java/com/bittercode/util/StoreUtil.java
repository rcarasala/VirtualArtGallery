package com.bittercode.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bittercode.model.UserRole;

/*
 * Store UTil File To Store Commonly used methods
 */
public class StoreUtil {

    /**
     * Check if the User is logged in with the requested role
     */
    public static boolean isLoggedIn(UserRole role, HttpSession session) {

        return session.getAttribute(role.toString()) != null;
    }

    /**
     * Modify the active tab in the page menu bar
     */
    public static void setActiveTab(PrintWriter pw, String activeTab) {

        pw.println("<script>document.getElementById(activeTab).classList.remove(\"active\");activeTab=" + activeTab
                + "</script>");
        pw.println("<script>document.getElementById('" + activeTab + "').classList.add(\"active\");</script>");

    }

    /**
     * Add/Remove/Update Item in the cart using the session
     */
    public static void updateCartItems(HttpServletRequest req) {
        String selectedArtId = req.getParameter("selectedArtId");
        HttpSession session = req.getSession();
        if (selectedArtId != null) { 
            String items = (String) session.getAttribute("items");
            if (req.getParameter("addToCart") != null) { 
                if (items == null || items.length() == 0)
                    items = selectedArtId;
                else if (!items.contains(selectedArtId))
                    items = items + "," + selectedArtId; 
                session.setAttribute("items", items);

                int itemQty = 0;
                if (session.getAttribute("qty_" + selectedArtId) != null)
                    itemQty = (int) session.getAttribute("qty_" + selectedArtId);
                itemQty += 1;
                session.setAttribute("qty_" + selectedArtId, itemQty);
            } else {
                int itemQty = 0;
                if (session.getAttribute("qty_" + selectedArtId) != null)
                    itemQty = (int) session.getAttribute("qty_" + selectedArtId);
                if (itemQty > 1) {
                    itemQty--;
                    session.setAttribute("qty_" + selectedArtId, itemQty);
                } else {
                    session.removeAttribute("qty_" + selectedArtId);
                    items = items.replace(selectedArtId + ",", "");
                    items = items.replace("," + selectedArtId, "");
                    items = items.replace(selectedArtId, "");
                    session.setAttribute("items", items);
                }
            }
        }

    }
}
