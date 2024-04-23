package com.bittercode.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.ArtsDBConstants;
import com.bittercode.model.Art;
import com.bittercode.model.StoreException;
import com.bittercode.service.ArtService;
import com.bittercode.util.DBUtil;

public class ArtServiceImpl implements ArtService {

    private static final String getAllArtsQuery = "SELECT * FROM " + ArtsDBConstants.TABLE_ART;
    private static final String getArtByIdQuery = "SELECT * FROM " + ArtsDBConstants.TABLE_ART
            + " WHERE " + ArtsDBConstants.COLUMN_BARCODE + " = ?";

    private static final String deleteArtByIdQuery = "DELETE FROM " + ArtsDBConstants.TABLE_ART+ "  WHERE "
            + ArtsDBConstants.COLUMN_BARCODE + "=?";

    private static final String addArtQuery = "INSERT INTO " + ArtsDBConstants.TABLE_ART + "  VALUES(?,?,?,?,?)";

    private static final String updateArtQtyByIdQuery = "UPDATE " + ArtsDBConstants.TABLE_ART + " SET "
            + ArtsDBConstants.COLUMN_QUANTITY + "=? WHERE " + ArtsDBConstants.COLUMN_BARCODE
            + "=?";

    private static final String updateArtByIdQuery = "UPDATE " + ArtsDBConstants.TABLE_ART+ " SET "
            + ArtsDBConstants.COLUMN_NAME + "=? , "
            + ArtsDBConstants.COLUMN_ARTIST + "=?, "
            + ArtsDBConstants.COLUMN_PRICE + "=?, "
            + ArtsDBConstants.COLUMN_QUANTITY + "=? "
            + "  WHERE " + ArtsDBConstants.COLUMN_BARCODE
            + "=?";

    @Override
    public Art getArtById(String artId) throws StoreException {
        Art art = null;
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(getArtByIdQuery);
            ps.setString(1, artId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bArtist = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                art = new Art(bCode, bName, bArtist, bPrice, bQty);
            }
        } catch (SQLException e) {

        }
        return art;
    }

    @Override
    public List<Art> getAllArts() throws StoreException {
        List<Art> arts = new ArrayList<Art>();
        Connection con = DBUtil.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(getAllArtsQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bArtist = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                Art art = new Art(bCode, bName, bArtist, bPrice, bQty);
                arts.add(art);
            }
        } catch (SQLException e) {

        }
        return arts;
    }

    @Override
    public String deleteArtById(String artId) throws StoreException {
        String response = ResponseCode.FAILURE.name();
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(deleteArtByIdQuery);
            ps.setString(1, artId);
            int k = ps.executeUpdate();
            if (k == 1) {
                response = ResponseCode.SUCCESS.name();
            }
        } catch (Exception e) {
            response += " : " + e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public String addArt(Art art) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(addArtQuery);
            ps.setString(1, art.getBarcode());
            ps.setString(2, art.getName());
            ps.setString(3, art.getArtist());
            ps.setDouble(4, art.getPrice());
            ps.setInt(5, art.getQuantity());
            int k = ps.executeUpdate();
            if (k == 1) {
                responseCode = ResponseCode.SUCCESS.name();
            }
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public String updateArtQtyById(String artId, int quantity) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(updateArtQtyByIdQuery);
            ps.setInt(1, quantity);
            ps.setString(2, artId);
            ps.executeUpdate();
            responseCode = ResponseCode.SUCCESS.name();
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public List<Art> getArtsByCommaSeperatedArtIds(String commaSeperatedArtIds) throws StoreException {
        List<Art> arts = new ArrayList<Art>();
        Connection con = DBUtil.getConnection();
        try {
            String getArtsByCommaSeperatedArtIdsQuery = "SELECT * FROM " + ArtsDBConstants.TABLE_ART
                    + " WHERE " +
                    ArtsDBConstants.COLUMN_BARCODE + " IN ( " + commaSeperatedArtIds + " )";
            PreparedStatement ps = con.prepareStatement(getArtsByCommaSeperatedArtIdsQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bArtist = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                Art art = new Art(bCode, bName, bArtist, bPrice, bQty);
                arts.add(art);
            }
        } catch (SQLException e) {

        }
        return arts;
    }

    @Override
    public String updateArt(Art art) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(updateArtByIdQuery);
            ps.setString(1, art.getName());
            ps.setString(2, art.getArtist());
            ps.setDouble(3, art.getPrice());
            ps.setInt(4, art.getQuantity());
            ps.setString(5, art.getBarcode());
            ps.executeUpdate();
            responseCode = ResponseCode.SUCCESS.name();
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

}
