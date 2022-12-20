package com.example.flotte.avion;

import com.example.flotte.kilometrage.Kilometrage;
import com.example.flotte.photo.Photo;
import dao.GenericDAO;
import frame.AnnotMap;
import frame.Attribut;
import utils.Connect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

@AnnotMap(nomTable="avion")
public class Avion {
    @Attribut(attr="idavion",primary_key=true)
    String idAvion;
    @Attribut(attr="idmarque")
    Integer idMarque;
    @Attribut(attr="idphoto")
    Integer idPhoto;
    @Attribut(attr="numeromatricule")
    String numeroMatricule;
    @Attribut(attr="datedebut")
    Date dateDebut;

    public Avion() {
    }

    public Avion(String idAvion, String numeroMatricule, Date dateDebut) {
        this.idAvion = idAvion;
        this.numeroMatricule = numeroMatricule;
        this.dateDebut = dateDebut;
    }

    public Avion(String numeroMatricule, Date dateDebut) {
        this.numeroMatricule = numeroMatricule;
        this.dateDebut = dateDebut;
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

    public int getIdMarque() {
        return idMarque;
    }

    public void setIdMarque(Integer idMarque) {
        this.idMarque = idMarque;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(Integer idPhoto) {
        
       if(idPhoto==null)this.idPhoto=new Integer(0);
       else this.idPhoto = idPhoto;
    }

    @Override
    public String toString() {
        return "Avion{" +
                "idAvion='" + idAvion + '\'' +
                ", idMarque=" + idMarque +
                ", idPhoto=" + idPhoto +
                ", numeroMatricule='" + numeroMatricule + '\'' +
                ", dateDebut=" + dateDebut +
                '}';
    }
    public Kilometrage[] listKilometrage(Connect conn)throws Exception{
        ArrayList<Kilometrage> liste=new ArrayList<>();
        String sql="SELECT * from kilometrage where idavion=?";
        PreparedStatement stat=null;
        ResultSet res=null;
        try {
            conn.getConnection();
            stat=conn.prepareStatement(sql);
            stat.setString(1,this.getIdAvion());
            res=stat.executeQuery();
            while(res.next()) {
                Kilometrage kilo=new Kilometrage();
                kilo.setDatekilometrage(new java.util.Date(res.getDate("datekilometrage").getTime()));
                kilo.setDebutkm(res.getDouble("debutkm"));
                kilo.setFinkm(res.getDouble("finkm"));
                kilo.setIdkilometrage(res.getString("idkilometrage"));
                kilo.setIdavion(res.getString("idavion"));
                liste.add(kilo);
            }
        }finally {
            if(stat!=null)stat.close();
            if(res!=null)res.close();
            conn.close();
        }
        Kilometrage[]kilo=new Kilometrage[liste.size()];
        return liste.toArray(kilo);
    }
    
       public void updatePhoto(String photo)throws Exception{
        Photo newphoto = new Photo(photo);
        GenericDAO.save(newphoto,new Connect());
        Integer val=GenericDAO.getLastId(newphoto,new Connect());
        this.setIdPhoto(val);
        GenericDAO.update(this,new Connect());
    }
}
