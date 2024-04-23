package com.bittercode.service;

import java.util.List;

import com.bittercode.model.Art;
import com.bittercode.model.StoreException;

public interface ArtService {

    public Art getArtById(String ArtId) throws StoreException;

    public List<Art> getAllArts() throws StoreException;

    public List<Art> getArtsByCommaSeperatedArtIds(String commaSeperatedArtIds) throws StoreException;

    public String deleteArtById(String artId) throws StoreException;

    public String addArt(Art art) throws StoreException;

    public String updateArtQtyById(String artId, int quantity) throws StoreException;
    
    public String updateArt(Art art) throws StoreException;

}
