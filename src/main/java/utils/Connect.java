/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author FITIA ARIVONY
 */
public final class Connect {
    Connection connect;
    boolean ouvert;
    boolean willbeused;

    public boolean Willbeused() {
        return willbeused;
    }

    public void setuses(boolean willbeused) {
        this.willbeused = willbeused;
    }

    public Connection getConnect() {
        return connect;
    }

    public void setConnect(Connection connect) {
        this.connect = connect;
    }

    public boolean isOuvert() {
        return ouvert;
    }

    public void setOuvert(boolean ouvert) {
        this.ouvert = ouvert;
    }
    public void close() throws SQLException{
        this.setOuvert(false);
      //  System.out.println(this.Willbeused());
        if(this.getConnect()!=null  && !this.Willbeused()) {
        	this.getConnect().close();
        	System.gc();
        }
    }
    public void commit() throws SQLException{
        if(this.getConnect()!=null){
            this.getConnect().commit();
            this.close();
        }
        
    }
     public void rollback() throws SQLException{
        if(this.getConnect()!=null){
            this.getConnect().rollback();
            this.close();
        }
    }
    public void setAutoCommit(boolean autocommit) throws SQLException{
        this.getConnect().setAutoCommit(autocommit);
    }
    public boolean getAutoCommit() throws SQLException{
        return this.getConnect().getAutoCommit();
    }
    public Connect() throws ClassNotFoundException, SQLException{
        System.gc();
        this.setOuvert(false);
        this.setuses(false);
        this.getConnection();
    }
   public boolean isNull() throws SQLException{
       if(this.getConnect()==null || this.isClosed())return true;
       return false;
   }
   public boolean isClosed() throws SQLException{
       return this.getConnect().isClosed();
   }
   public PreparedStatement prepareStatement(String sql) throws Exception{
       this.getConnection();
       return this.getConnect().prepareStatement(sql);
   }
   public Statement createStatement(String sql) throws Exception{
       this.getConnection();
       return this.getConnect().createStatement();
   }
   
     public  void getConnection()throws ClassNotFoundException,SQLException{
        // System.out.println("hereee");
         if(this.isNull() || this.isClosed() || !this.Willbeused()){
//             Class.forName("org.postgresql.Driver");
        	 String url="jdbc:postgresql://rogue.db.elephantsql.com/ksurybch";
        	  //return DriverManager.getConnection(url,"ijwqupmw","Akt9J-YuWSdpgLDbrtR7pTr-OIQ5gA4D");
               //this.setConnect(DriverManager.getConnection("jdbc:postgresql://localhost:8089/flotteavion" ,"postgres","ROOT" ));
        	  this.setConnect(DriverManager.getConnection(url,"ksurybch","2yzMq6JGp1hmHCR0WnT6BjtG3sQE-ybt" ));

         }
    }
    public void forceClose() throws SQLException{
        this.setuses(false);
        this.close();
    }
    @Override
    public String toString() {
        return "Connect{" + "connect=" + connect + ", ouvert=" + ouvert + '}';
    }
    public boolean insert_update(String sql,PreparedStatement stat) throws Exception {	
		 
		 try {
			 if(stat==null)stat=this.prepareStatement(sql);
			  stat.executeUpdate();
		        
		 }catch(Exception ex){
			throw ex;
		 }
		 finally {
			 if(stat!=null)stat.close();
			 this.close();
		 }
			return true;   
    }
    
   
    
     
    
}
