package com.example.flotte.kilometrage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import utils.Connect;

public class Kilometrage {
    String idkilometrage;
    String idavion;
    Date datekilometrage;
    Double debutkm;
    Double finkm;
    public String getIdkilometrage() {
        return idkilometrage;
    }
    public void setIdkilometrage(String idkilometrage) {
        this.idkilometrage = idkilometrage;
    }
    public Date getDatekilometrage() {
        return datekilometrage;
    }
    public void setDatekilometrage(Date datekilometrage) {
        this.datekilometrage = datekilometrage;
    }
    public Double getDebutkm() {
        return debutkm;
    }
    public void setDebutkm(Double debutkm) {
        this.debutkm = debutkm;
    }
    public Double getFinkm() {
        return finkm;
    }
    public void setFinkm(Double finkm) {
        this.finkm = finkm;
    }
    public String getIdavion() {
        return idavion;
    }
    public void setIdavion(String idavion) {
        this.idavion = idavion;
    }
    public boolean insert(Connect conn)throws Exception{
        String sql="INSERT INTO kilometrage(idavion,datekilometrage,debutkm,finkm) values (?,?,?,?)";
        try {
            conn.getConnection();
            PreparedStatement stat=conn.prepareStatement(sql);
            stat.setString(1, this.getIdavion());
            stat.setDate(2,new java.sql.Date(this.getDatekilometrage().getTime()));
            stat.setDouble(3,this.getDebutkm());
            stat.setDouble(4,this.getFinkm());

            conn.insert_update(sql,stat);
            return true;
        }catch(Exception e) {

            throw e;
        }
        finally {
            conn.close();
        }
    }
    public Kilometrage[] selectAll(Connect conn)throws Exception{
        String sql="SELECT * from kilometrage";
        PreparedStatement stat=null;
        ResultSet res=null;
        try {
            conn.getConnection();
            stat=conn.prepareStatement(sql);
            res=stat.executeQuery();
            ArrayList<Kilometrage>kilos=new ArrayList<>();
            while(res.next()) {
                Kilometrage kilo=new Kilometrage();
                kilo.setDatekilometrage(new Date(res.getDate("datekilometrage").getTime()));
                kilo.setIdkilometrage(res.getString("idkilometrage"));
                kilo.setDebutkm(res.getDouble("debutkm"));
                kilo.setFinkm(res.getDouble("finkm"));
                kilo.setIdavion(res.getString("idavion"));
                kilos.add(kilo);
            }
            Kilometrage[] kilo=new Kilometrage[kilos.size()];
            return kilos.toArray(kilo);
        }catch(Exception e) {
            throw e;
        }finally {
            if(res!=null)res.close();
            if(stat!=null)stat.close();
            conn.close();
        }
    }
    public void update(Connect conn) throws Exception {

        PreparedStatement pStatement = null;
        String sql = "update kilometrage set datekilometrage=? ,  debutkm=? ,  finkm =? where idkilometrage=?";
        try {
            conn.getConnection();
            pStatement = conn.prepareStatement(sql);
            pStatement.setDate(1,new java.sql.Date(this.getDatekilometrage().getTime()));
            pStatement.setDouble(2,this.getDebutkm());
            pStatement.setDouble(3,this.getFinkm());
            pStatement.setString(4,this.getIdkilometrage());
            pStatement.execute();
        } finally {

            if (pStatement != null)pStatement.close();
            conn.close();
        }
    }

    public void delete(Connect conn) throws Exception {
        PreparedStatement pStatement = null;
        String sql = "delete from kilometrage where idkilometrage=?";
        try {
            conn.getConnection();
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1,getIdkilometrage());
            pStatement.execute();
        } finally {
            if (pStatement != null)pStatement.close();
            conn.close();
        }
    }
}
