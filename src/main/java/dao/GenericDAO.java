/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import frame.Attribut;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import utils.Connect;
import utils.GenericHelper;
import static utils.GenericHelper.get;

/**
 *
 * @author FITIA ARIVONY
 */
public class GenericDAO {
    public static boolean save(Object o,Connect conn)throws Exception{
        PreparedStatement stat=null;
        
        try{
           conn.getConnection(); 
            String sql="INSERT INTO "+GenericHelper.getNomTable(o)+"(";
            ArrayList<Field>fields=GenericHelper.inserer(o);
            for(int i=0;i<fields.size();i++){
                if(i==fields.size()-1){
                    sql+=GenericHelper.isAttr(fields.get(i))+")";
                }else{            
                    sql+=GenericHelper.isAttr(fields.get(i))+",";
                }
            }
         
        sql+=" values (";
       
        ArrayList<Object>valeurs=GenericHelper.valeurs(o);
        for(int i=0;i<valeurs.size();i++){
            if(i==fields.size()-1){
                    sql+="? )";
                }else{            
                    sql+="? ,";
                }
        }
        stat=conn.prepareStatement(sql);
          for(int i=0,j=1;i<valeurs.size();i++,j++){
              stat.setObject(j,valeurs.get(i));
        }
        System.out.println(stat);
          stat.executeUpdate();
         return true;
        }catch(Exception e){
            throw e;
        }finally{
           
            if(stat!=null)stat.close();
            conn.close();   
        }   
    }
     public static boolean update(Object o,Connect conn) throws  Exception{
     
        PreparedStatement stat=null;
         String sql="UPDATE "+GenericHelper.getNomTable(o)+" set ";
        try{
             conn.getConnection(); 
             ArrayList<Field> attributs=GenericHelper.inserer(o);
             for(int i=0;i<attributs.size();i++){
                 sql+=GenericHelper.isAttr(attributs.get(i))+" = ";
                 if(i!=attributs.size()-1){
                   sql+="? ,";
                }else {
                     sql+="? ";
                 }
             }
             sql+="   where  ";
            sql+=GenericHelper.isPrimary(GenericHelper.getPrimary(o))+"= ? ";
             stat=conn.prepareStatement(sql);
            ArrayList<Object> valeurs=GenericHelper.valeurs(o);
            int j;
            int i;
           for(i=0,j=1;i<valeurs.size();i++,j++){
              
              stat.setObject(j,valeurs.get(i));
        }    
              stat.setString(j,GenericHelper.getPrimaryValue(o));
           System.out.println(stat);
            stat.executeUpdate();      
        }catch(Exception e){
           return false;
        }finally{
            if(stat!=null) stat.close();
            conn.close();
        }
        return true;
    }
     public static boolean delete(Object o,Connect conn) throws Exception{
         PreparedStatement stat=null;
         String sql="DELETE FROM "+GenericHelper.getNomTable(o);
         try{
             sql+="  where ";
            sql+=GenericHelper.isPrimary(GenericHelper.getPrimary(o))+"= ? ";
            conn.getConnection(); 
              stat=conn.prepareStatement(sql);
              stat.setString(1,GenericHelper.getPrimaryValue(o));
           System.out.println(stat);
           stat.executeUpdate();
         }catch(Exception e){
             e.printStackTrace();
             return false;
         }finally{
             if(stat!=null)stat.close();
             if(conn!=null)conn.close();
         }
         return true;
     }
     public static ArrayList<Object> selectAll(Object o,Connect conn) throws Exception{
         String sql="SELECT * from "+GenericHelper.getNomTable(o);
         PreparedStatement stat=null;
         ResultSet res=null;
         Class cl=o.getClass();
         ArrayList<Object>liste=new ArrayList<>();
         try{
             conn.getConnection(); 
             stat=conn.prepareStatement(sql);
             res=stat.executeQuery();
             liste=GenericDAO.extractResult(res, o);
            
        }      
         catch(Exception e){
           throw e;
         }finally{
             if(res!=null)res.close();
             if(stat!=null)stat.close();
              conn.close();   
         }
         return liste;
     }
     
      public static ArrayList<Object> getById(Object o,Connect conn) throws Exception{
         String sql="SELECT * from "+GenericHelper.getNomTable(o);
         
         PreparedStatement stat=null;
         ResultSet res=null;
         Class cl=o.getClass();
         ArrayList<Object>liste=new ArrayList<>();
         try{
             sql+="  where ";
            sql+=GenericHelper.isPrimary(GenericHelper.getPrimary(o))+"= ? ";
            //System.out.println(sql);
            String valeur=GenericHelper.getPrimaryValue(o);
            conn.getConnection(); 
              stat=conn.prepareStatement(sql);
              stat.setString(1,valeur);
             res=stat.executeQuery();
             liste=GenericDAO.extractResult(res, o);             
         }catch(Exception e){
            e.printStackTrace();
         }finally{
             if(res!=null)res.close();
             if(stat!=null)stat.close();
             conn.close();   
         }
         return liste;
     }
      

         public static ArrayList get(Object o,Connect con) throws Exception{
        Class cl=o.getClass();
        ArrayList <String> listChamp=new ArrayList<>();
        ArrayList listObj=new ArrayList();
        String sql="SELECT * FROM ";
        con.getConnection(); 
        if(GenericHelper.getNomTable(o)!=null){
            sql+=GenericHelper.getNomTable(o);
        }
        else{
            sql+=o.getClass().getSimpleName() ;
        }
        sql+=" where 1=1 ";
        ArrayList<Field>nonNull=GenericHelper.getNonull(o);
        Field[] liFi=new Field[nonNull.size()];
        liFi=nonNull.toArray(liFi);
        for(int i=0;i<liFi.length;i++){
            try{
                Attribut cn=GenericHelper.getAttribut(liFi[i]);
                 Object tempo=GenericHelper.get(o,cn.attr());
                listChamp.add(cn.attr());
                    if(cn.foreign_key()){
                            Class clTemp=tempo.getClass();
                            Field[] liMeth=GenericHelper.listAllFields(clTemp);
                            for(int j=0;j<liMeth.length;j++){
                                Attribut tempCN=GenericHelper.getAttribut(liMeth[j]);
                                if(tempCN.primary_key()){
                                    Method getID=clTemp.getMethod("get"+GenericHelper.toUpperCaseFirst(liMeth[j].getName()));
                                    listObj.add(getID.invoke(tempo));
                                }
                            }
                        }else listObj.add(tempo);
            }
            catch(Exception e){
                throw e;
            }
            
        }
        for(int i=0;i<listChamp.size();i++){
            sql+=" and "+listChamp.get(i) +"=? ";
        }
//        System.out.println(sql);
        PreparedStatement stmt=con.prepareStatement(sql);
        for(int i=1;i<listChamp.size()+1;i++){
            stmt.setString(i, listObj.get(i-1).toString());
        }
//        System.out.println(stmt);
        ResultSet RS=stmt.executeQuery();
        ArrayList res=new ArrayList();
       
        try{
            while(RS.next()){
                //o.getClass().newInstance();
                Object otemp = cl.newInstance();
               
                for(int i=0;i<liFi.length;i++){
                     Attribut cn=GenericHelper.getAttribut(liFi[i]);
                    if(!cn.foreign_key())GenericHelper.setValeur(otemp, liFi[i], RS.getObject(cn.attr()));
                        else{// if foreign key
                            Object tempOb=liFi[i].getType().newInstance();
                            Field[] liFi2=GenericHelper.listAllFields(tempOb.getClass());
                            for(int j=0;j<liFi2.length;j++){
                                Attribut cn2=GenericHelper.getAttribut(liFi2[j]);
                                if(cn2!=null && cn2.primary_key()){
                                    Method get2=o.getClass().getMethod("get"+GenericHelper.toUpperCaseFirst(liFi[i].getName()));//get objet
                                    Method get=tempOb.getClass().getMethod("get"+GenericHelper.toUpperCaseFirst(liFi2[j].getName()));//get id
                                    Method set2=tempOb.getClass().getMethod("set"+GenericHelper.toUpperCaseFirst(liFi2[j].getName()),liFi2[j].getType());//set id
                                    set2.invoke(tempOb, RS.getString(liFi[i].getName()));
                                     Method set=o.getClass().getMethod("set"+GenericHelper.toUpperCaseFirst(liFi[i].getName()),liFi[i].getType());
                                    set.invoke(otemp, liFi[i].getType().cast(GenericDAO.getById(tempOb, con).get(0)) );
                                    break;
                                }
                            }
                        }
                    
                }
                res.add(otemp);
            }
        }
        catch(Exception e){
           throw e;
        }finally {
            if(RS!=null)RS.close();
            if(stmt!=null)stmt.close();
            con.close();
        }
        return res;
    }
    
    public static Object[] getByIds(Object o,Connect conn) throws Exception{
    
        HashMap<Field,Object>valeurs=new HashMap<Field,Object>();
        ArrayList<Field>fields=GenericHelper.getNonull(o);
            for(int i=0;i<fields.size();i++)valeurs.put(fields.get(i),GenericHelper.get(o,fields.get(i).getName()));
       String sql="SELECT * FROM "+GenericHelper.getNomTable(o);
       for(int i=0;i<fields.size();i++){
           sql=GenericDAO.getWhere(i, sql);
           sql+=GenericHelper.isAttr(fields.get(i))+"= ? ";
           
       }
       PreparedStatement stat=null;
         ResultSet res=null;
       try{
           conn.getConnection(); 
           stat=conn.prepareStatement(sql);
           for(int i=0,j=1;i<fields.size();i++,j++){
               if(valeurs.get(fields.get(i)) instanceof Date)
               {
                   SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                   stat.setString(j,format.format((Date)valeurs.get(fields.get(i))));
               }else  stat.setString(j,valeurs.get(fields.get(i)).toString());
                
              
           }
         res=stat.executeQuery();
         System.out.println(stat);
         return GenericDAO.extractResult(res, o).toArray();
       }
       finally{
//               System.out.println(sql);
        if(res!=null)res.close();
        if(stat!=null)stat.close();
        conn.close();
    }
       
        
    }
    public static ArrayList<Object> extractResult(ResultSet res,Object o)throws Exception{
        Class cl=o.getClass();
        ArrayList<Object>liste=new ArrayList<Object>();
        ArrayList<Field>colonne=GenericHelper.choisir(o);
         while(res.next()){     
             
                 Object valiny=new Object();
                valiny=cl.newInstance();
            for(int i=0;i<colonne.size();i++){
               System.out.println(colonne.get(i).getName()+"----");
               if(res.getObject((GenericHelper.getAttribut(colonne.get(i)).attr()))!=null){
                   GenericHelper.setValeur(valiny,colonne.get(i),res.getObject((GenericHelper.getAttribut(colonne.get(i)).attr())));
               }
               
            }
            
            liste.add(valiny);
        }
         return liste;
    }
    
    public static String getWhere(Integer i,String sql){
        if(i==0){
            sql+=" where ";
            return sql;
        }
        sql+=" and ";
        return sql;
    }
     public static Integer getLastId(Object obj,Connect conn) throws Exception{
         String primary=GenericHelper.isPrimary(GenericHelper.getPrimary(obj));
       String sql="select "+primary+ "   dernier from "+GenericHelper.getNomTable(obj)+" order by "+primary+"  desc limit 1";
       PreparedStatement stat=null;
       ResultSet res=null;
       try{
           conn.getConnection();
           stat=conn.prepareStatement(sql);
           System.out.println(stat);
           res=stat.executeQuery();
           while(res.next())return res.getInt("dernier");
       }catch(Exception e){
           throw e;
       }finally{
           if(res!=null)res.close();
           if(stat!=null)stat.close();
       }
       throw new Exception("No last id");
   }
     public static ArrayList<Object> executeQuery(String sql,Connect conn,Object obj)throws Exception{
         PreparedStatement stat=null;
         ResultSet res=null;
         
        try{
            
             conn.getConnection();
             stat=conn.prepareStatement(sql);
             System.out.println(sql);
             res=stat.executeQuery();
             
             return GenericDAO.extractResult(res, obj);
         
        }finally{
            if(res!=null)res.close();
            if(stat!=null)stat.close();
            conn.close();
        }
     }

   
}
