
package com.example.flotte.connexion;

import java.sql.Connection;
import java.sql.DriverManager;
public class EtablirConnex{
	public static Connection getConnection() throws Exception{
//            Class.forName("org.postgresql.Driver");
             String url="jdbc:postgresql://rogue.db.elephantsql.com/ksurybch";
           return DriverManager.getConnection(url,"ksurybch","2yzMq6JGp1hmHCR0WnT6BjtG3sQE-ybt" );
	      //  return DriverManager.getConnection("jdbc:postgresql://localhost:5432/flotteavion" ,"postgres","root" );
    }
}