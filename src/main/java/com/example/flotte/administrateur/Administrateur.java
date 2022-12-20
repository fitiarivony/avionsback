package com.example.flotte.administrateur;

import com.example.flotte.connexion.Base;
import com.example.flotte.connexion.EtablirConnex;
import dao.GenericDAO;
import exception.OutOfConnectionException;
import frame.AnnotMap;
import frame.Attribut;
import utils.Connect;
import utils.SHA1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


@AnnotMap(nomTable="administrateur")
public class Administrateur {
	@Attribut(attr="idadmin")
    String idAdmin;
	@Attribut(attr="identifiant")
    String identifiant;

	@Attribut(attr="mdp")
    String mdp;
	@Attribut(attr="token")
    String token;
	@Attribut(attr="dateexpiration")
    Date dateExpiration;
    public String getIdentifiant() {
        return identifiant;
    }
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    public String getMdp() {
        return mdp;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Date getDateExpiration() {
        return dateExpiration;
    }
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public Administrateur(){}

    public Administrateur(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public Administrateur(String identifiant, String mdp) {
        this.setIdentifiant(identifiant);
        this.setMdp(mdp);
    }
    public boolean login(Connect conn) throws Exception {
        PreparedStatement stat=null;
        ResultSet res=null;
        String sql="SELECT * from administrateur where mdp=md5(?) and identifiant=?";
        try {
            stat=conn.prepareStatement(sql);
            stat.setString(1,this.getMdp());
            stat.setString(2,this.getIdentifiant());
            //System.out.println(stat);
            res=stat.executeQuery();
            while(res.next())return true;

            throw new Exception();
        }finally {
            if(stat!=null)stat.close();
            conn.close();
        }

    }
    public void logout() throws Exception{
        Connect connection = new Connect();
        connection.getConnection();
        PreparedStatement pStatement = null;
        String sql = "update administrateur set token='' where idAdmin=?";

        try{
            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, getIdAdmin());
            int ta=pStatement.executeUpdate();
            if(ta==0){throw new Exception();}
        }catch (Exception e){
            throw e;
        }finally{
            if(pStatement != null)pStatement.close();
            connection.close();
        }
    }
    public void setToken()throws Exception{
        Connection conn= EtablirConnex.getConnection();
        String sql="update administrateur set dateexpiration=current_timestamp+'00:15:00' where identifiant='"+identifiant+"'";
        Statement stat=conn.createStatement();
        stat.executeUpdate(sql);

        Object[][] obj=Base.select(new Administrateur(identifiant,Base.getMd5(mdp)),"administrateur",conn);

        String currentexpiration=obj[0][3].toString();
       
        String newtoken=Base.getMd5(identifiant+currentexpiration);
        SHA1 sha=new SHA1(identifiant+currentexpiration);
        newtoken=sha.getSha1();
        this.setToken(newtoken);
        this.setDateExpiration((Date)obj[0][4]);
        String sql1="update administrateur set token='"+newtoken+"' where identifiant='"+identifiant+"'";
        Statement stat1=conn.createStatement();
        System.out.println(sql1);
        stat.executeUpdate(sql1);
        stat1.close();
        conn.close();
        System.gc();

    }
   
    public Administrateur checkConnex(Connect conn)throws Exception{
    	String sql="SELECT * from administrateur where  token=? and dateexpiration>current_timestamp";
    	PreparedStatement stat=null;
    	try {
    		Administrateur admin=this.init(this.getToken(), conn);
    		conn.getConnection();
    		stat=conn.prepareStatement(sql);
    		stat.setString(1,this.getToken());
    		System.out.println(stat);
    		ArrayList<Object>liste=GenericDAO.executeQuery(stat.toString(), conn, new Administrateur());
    		if(liste.isEmpty())throw new OutOfConnectionException("Connexion expiree",230);
    		Administrateur adm=(Administrateur)liste.get(0);
    		return adm;
    	}finally {
    		if(stat!=null)stat.close();
    		conn.close();
    	}
    	
    }
    
    public Administrateur init(String token,Connect conn)throws Exception{
    	
    	try {
    		conn.getConnection();
    		Administrateur adm=new Administrateur();
    		adm.setToken(token);
    		Object[]val=GenericDAO.getByIds(adm, conn);
    		if(val.length==0)throw new OutOfConnectionException("Cet admin n'existe pas",230);
    		adm=(Administrateur)val[0];
    		return adm;
    	}finally {
    		conn.close();
    	}
    }
}
