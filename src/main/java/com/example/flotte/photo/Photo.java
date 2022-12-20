/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.flotte.photo;

import frame.AnnotMap;
import frame.Attribut;

/**
 *
 * @author FITIA ARIVONY
 */
@AnnotMap(nomTable="photo_avion")
public class Photo{
    @Attribut(attr="id",primary_key=true)
    Integer idphoto;
    @Attribut(attr="photo")
    String photo;

    public Integer getIdphoto() {
        return idphoto;
    }

    public void setIdphoto(Integer idphoto) {
        this.idphoto = idphoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
     public Photo(Integer idphoto) {
        this.idphoto = idphoto;
    }
      public Photo(String photo) {
        this.photo = photo;
    }
      public Photo() {
        
    }

}
