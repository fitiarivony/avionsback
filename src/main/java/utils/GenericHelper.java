/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import frame.AnnotMap;
import frame.Attribut;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FITIA ARIVONY
 */
public class GenericHelper {
    public  static String getNomTable(Object o)throws NullPointerException{
        try{
             AnnotMap annotation=(AnnotMap) o.getClass().getAnnotation(AnnotMap.class);
        return annotation.nomTable(); 
        }catch(Exception e){
            throw e;
        }
      
    }
     public static boolean isAnnoted(Field o){
     
            Attribut annotation=(Attribut)o.getAnnotation(Attribut.class);  
            
            if(annotation!=null){
                
             return true;
            }
            return false;
            
    }
   
    public static Field[] getFieldAnnoted(Object o){
          Class cl=o.getClass();
          Field[]fields=GenericHelper.listAllFields(cl);
           ArrayList<Field>inserer=new ArrayList<>();
            for(int i=0;i<fields.length;i++){
            
            if(GenericHelper.isAnnoted(fields[i])){
                inserer.add(fields[i]);
            }
        }
            Field[]attr=new Field[inserer.size()];
       return inserer.toArray(attr);
    }
     public static String isAttr(Field o)throws Exception{
     
            Attribut annotation=(Attribut)o.getAnnotation(Attribut.class);  
            
            if(annotation!=null && !annotation.primary_key()){
                
             return annotation.attr();   
            }
           return null;
    }
    
    
    public static String isPrimary(Field o)throws Exception{
         Attribut annotation=(Attribut)o.getAnnotation(Attribut.class);  
            
            if(annotation!=null && annotation.primary_key()){
                
             return annotation.attr();   
            }
            return null;
    }
    public static ArrayList<Field> inserer(Object o)throws Exception{
        Class cl=o.getClass();
        Field[]fields=GenericHelper.getFieldAnnoted(o);
        ArrayList<Field>inserer=new ArrayList<>();
        for(int i=0;i<fields.length;i++){
            
            if(GenericHelper.isAttr(fields[i])!=null){
                inserer.add(fields[i]);
            }
        }
        return inserer;
    }
    
      public static Field getPrimary(Object o)throws Exception{
        Class cl=o.getClass();
        Field[]fields=cl.getDeclaredFields();
        ArrayList<Field>inserer=new ArrayList<>();
        for(int i=0;i<fields.length;i++){
            
            if(GenericHelper.isPrimary(fields[i])!=null){
                return fields[i];
            }
        }
        throw new Exception("No primary key");
    }
   
    public static ArrayList<Object> valeurs(Object o) throws Exception{
        Class cl=o.getClass();
        ArrayList<Field>inserer=inserer(o);
        ArrayList<Object> valeur=new ArrayList<>();
        for(int i=0;i<inserer.size();i++){
            
            if(get(o,inserer.get(i).getName()) instanceof Date){
                SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date daty=(Date)get(o,inserer.get(i).getName());
                valeur.add(new java.sql.Date(daty.getTime()));
            }else{
                valeur.add((get(o,inserer.get(i).getName())));
            }
            
        }
        return valeur;
    }
    
    public static String getPrimaryValue(Object o) throws Exception{
        Class cl=o.getClass();
        Field inserer=GenericHelper.getPrimary(o);
            if(get(o,inserer.getName()) instanceof Date){
                SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date daty=(Date)get(o,inserer.getName());
                return (format.format(daty));
            }
                return (get(o,inserer.getName())).toString();
            
    }
    
    public static String getString(Object o,Field attribut) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
       
        if(GenericHelper.get(o, attribut.getName()) instanceof Date){
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                Date daty=(Date)get(o,attribut.getName());
               
                return format.format(daty);
        }
        return get(o,attribut.getName()).toString();
    }
    
    public static Object get(Object o,String attribut) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Class cl=o.getClass();
        Object obj=new Object();
        String fonction=attribut.substring(0, 1).toUpperCase()+attribut.substring(1);
        try {
            Method meth=cl.getMethod("get"+fonction,null);
            obj=meth.invoke(o,null);
        } catch (NoSuchMethodException ex) {
          Method[]methods=cl.getMethods();
                for(int j=0;j<methods.length;j++){
                    if(methods[j].getName().toLowerCase().equalsIgnoreCase("get"+attribut.toLowerCase())){
                        obj=methods[j].invoke(o,null);
                        break;
                    }
                }
        } 
        return obj;
    }
    public static Connection getConnection(Connection conn)throws ClassNotFoundException,SQLException{
         Class.forName("org.postgresql.Driver");
         if(conn==null){
               conn=DriverManager.getConnection("jdbc:postgresql://localhost:8089/framework","societe","mdp");
         }
      return conn;
    }
    
  
    //select
      public static Attribut getAttribut(Field o)throws Exception{
     
            Attribut annotation=(Attribut)o.getAnnotation(Attribut.class);  
            
            if(annotation!=null){
                
             return annotation;  
            }
            throw new  Exception("It is not an attribut in your table");
            
    }
      
    public static ArrayList<Field> choisir(Object o)throws Exception{
        Class cl=o.getClass();
        Field[]fields=cl.getDeclaredFields();
        ArrayList<Field>inserer=new ArrayList<>();
        for(int i=0;i<fields.length;i++){
            
            if(GenericHelper.getAttribut(fields[i])!=null){
                
                inserer.add(fields[i]);
            }
        }
        return inserer;
    }
    public static void setValeur(Object o,Field attribut ,Object valeur) throws Exception{
        Class cl=o.getClass();
       
         String fonction=attribut.getName().substring(0, 1).toUpperCase()+attribut.getName().substring(1);
         
        try {
              
            Method meth=cl.getMethod("set"+fonction,attribut.getType());
        
       
            meth.invoke(o,valeur);
        } catch (NoSuchMethodException ex) {
          Method[]methods=cl.getMethods();
                for(int j=0;j<methods.length;j++){
                    if(methods[j].getName().toLowerCase().equalsIgnoreCase("set"+attribut.getName().toLowerCase())){
                        methods[j].invoke(o,valeur);
                        break;
                    }
                }
        } 
        
    }
    public static ArrayList<Field> getNonull(Object o)throws Exception{
        Field[]fields=o.getClass().getDeclaredFields();
        ArrayList<Field>nonull=new ArrayList<Field>(); 
        for(int i=0;i<fields.length;i++){
            if(GenericHelper.get(o, fields[i].getName())!=null)nonull.add(fields[i]);
        }
        return nonull;
    }
    public static Field[] listAllFields(Class cl){
        ArrayList<Field> fi=new ArrayList<>();
        
        fi.addAll(Arrays.asList(cl.getDeclaredFields()));
        while(cl.getSuperclass()!=Object.class){
            cl=cl.getSuperclass();
            fi.addAll(Arrays.asList(cl.getDeclaredFields()));
        }
        
        Field[] res=new Field[fi.size()];
        for(int i=0;i<res.length;i++)
            res[i]= fi.get(i);
        return res;
    }
        public static String toUpperCaseFirst(String name){
        char[] charLi=name.toCharArray();
        String res=""+charLi[0];
        res=res.toUpperCase();
        for(int i=1;i<charLi.length;i++){
            res+=charLi[i];
        }
        return res;
    }
    
    
    
}
