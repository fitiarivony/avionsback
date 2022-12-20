package com.example.flotte.avion;

import frame.AnnotMap;
import frame.Attribut;

import java.util.Date;

@AnnotMap(nomTable="avion_detail")
public class Avion_detail {
    @Attribut(attr="idavion")
    String idAvion;
    @Attribut(attr="numeromatricule")
    String numeroMatricule;
    @Attribut(attr="datedebut")
    Date dateDebut;
    @Attribut(attr="nom")
    String nom;
    @Attribut(attr="photo")
    String photo;

    public Avion_detail() {
    }

    public String getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(String idAvion) {
        this.idAvion = idAvion;
    }

    public String getNumeroMatricule() {
        return numeroMatricule;
    }

    public void setNumeroMatricule(String numeroMatricule) {
        this.numeroMatricule = numeroMatricule;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
