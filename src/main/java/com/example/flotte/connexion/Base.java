
package com.example.flotte.connexion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class Base{
    public static String concatWith(Vector ls,String t){
        String retour=ls.get(0).toString();
        for(int i=1;i<ls.size();i++){
            retour=retour+" "+t+" "+ls.get(i).toString();
        }
        return retour;
    }
    public static boolean isNumeric(String s){
        try{
            Integer i=Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            try{
                Float i=Float.parseFloat(s);
            }
            catch(NumberFormatException | NullPointerException nf){
                return false;
            }
        }
        return true;
    }
    public static boolean intable(String s){
        try{
            Integer i=Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            try{
                Float i=Float.parseFloat(s);
            }
            catch(NumberFormatException nf){
                return true;
            }
            return false;
        }
        return true;
    }
    public static boolean isNull(String s){
        if(!isNumeric(s)&&s==null){
            return true;
        }
        if(intable(s)&&isNumeric(s)&&Integer.valueOf(s)==0){
            return true;
        }
        if(!intable(s)&&isNumeric(s)&&Float.valueOf(s)==0.0){
            return true;
        }
        return false;
    }
    public static String treatment(String s){
        if(!isNumeric(s)&&!s.equals("current_date")){
            s="'"+s+"'";
        }
        return s;
    }
    public static void insert(Object o,String tname,String pk,Connection co) throws Exception{
        int conf=0;
        if(co==null){	co=(new EtablirConnex()).getConnection();	conf=1;}
        if(pk==null){	pk="id"+o.getClass().getSimpleName();	}
        String regex=","; String title=pk; String donne="";
        Class c=o.getClass();
        Field[] f=c.getDeclaredFields();
        for (Field f1 : f) {
            String value = treatment(c.getMethod("get" + maj(f1.getName())).invoke(o).toString());
            if (!f1.getName().equals(pk)) {
                title = title+regex + f1.getName();
                donne=donne+regex+value;
            }
        }
        String sc="select nextval('sc"+c.getSimpleName()+"')";
        String sql="insert into "+tname+" ("+title+") values(("+sc+")"+donne+")";
        Statement stat=co.createStatement();
        stat.executeUpdate(sql);
        if(conf==1){ co.close(); }
        stat.close();
    }
    public static void update(Object o,String tname,String pk,Connection co) throws Exception{
        int conf=0;
        if(co==null){	co=(new EtablirConnex()).getConnection();	conf=1;}
        if(pk==null){	pk="id"+o.getClass().getSimpleName();	}

        String equal="="; String id=""; Vector donne=new Vector();
        Class c=o.getClass();
        Field[] f=c.getDeclaredFields();
        for (Field f1 : f) {
            String value = treatment(c.getMethod("get" + maj(f1.getName())).invoke(o).toString());
            if (!f1.getName().equals(pk)) {
                donne.add(f1.getName() + equal + value);
            } else {
                id=pk+equal+value;
            }
        }
        String data=concatWith(donne,",");
        String sql="update "+tname+" set "+data+" where "+id;
        Statement stat=co.createStatement();
        stat.executeUpdate(sql);
        if(conf==1){ co.close(); }
        stat.close();
    }
    public static Object[][] select(Object filtre,String tname,Connection co) throws Exception{
        int conf=0;
        if(co==null){	co=(new EtablirConnex()).getConnection();	conf=1;}
        Vector donne=new Vector(); String and="and"; String sql; String value;
        Class c=filtre.getClass();
        Field[] f=c.getDeclaredFields();
        int check=0;
        for (Field f1 : f) {
            try {
                value = treatment(c.getMethod("get" + maj(f1.getName())).invoke(filtre).toString());
            }catch(NullPointerException npe){
                value=null;
            }
            if (!isNull(value)) {
                check++;
                donne.add(f1.getName() + "=" + value);
            }
        }
        if(check==0){
            sql="select * from "+tname;
        }else{
            String condition=concatWith(donne,"and");
            sql="select * from "+tname+" where "+condition;
            System.out.println(sql);
        }

        Statement stat=co.createStatement();
        ResultSet rs=stat.executeQuery(sql);
        SetResult sr=new SetResult(rs,sql);
        Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
        if(conf==1){ co.close(); }
        stat.close();
        return data;
    }
    public static Method getRightMethod(Method[] m ,String toDo, String attr){
        for (Method m1 : m) {
            if (m1.getName().equals(toDo.concat(attr))) {
                return m1;
            }
        }
        return null;
    }
    public static String maj(String s){
        return s.substring(0,1).toUpperCase().concat(s.substring(1));
    }
    public static String getCurrentDate() throws Exception{
        String sql="select current_date";
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement(sql);
            rs=stat.executeQuery();
            SetResult sr=new SetResult(rs,sql);
            Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
            retour=data[0][0].toString();
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
        return retour;
    }
    public static String getMd5(String key) throws Exception{
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement("select md5(?)");
            stat.setString(1,key);
            rs=stat.executeQuery();
            while(rs.next()){
                retour=rs.getString(1);
            }
            return retour;
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
    }
    public static  int getSc(String sc) throws Exception{
        int retour=0;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement("select nextval(?)");
            stat.setString(1,sc);
            rs=stat.executeQuery();
            while(rs.next()){
                retour=rs.getInt(1);
            }
            return retour;
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
    }
    public static  int diff_daty(String date1,String date2) throws Exception{
        Connection co=(new EtablirConnex()).getConnection();
        String sql="select to_date('"+date2+"','yyyy-mm-dd') - to_date('"+date1+"','yyyy-mm-dd') ";
        PreparedStatement stat=co.prepareStatement(sql);
        ResultSet rs=stat.executeQuery();
        SetResult sr=new SetResult(rs,sql);
        Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
        stat.close();
        co.close();
        return Integer.valueOf(data[0][0].toString());
    }
    public static  String datyPlus(String date,int nbreJour) throws Exception{
        Connection co=(new EtablirConnex()).getConnection();
        String sql="select to_date('"+date+"','yyyy-mm-dd')+"+nbreJour;
        PreparedStatement stat=co.prepareStatement(sql);
        System.out.println(stat.toString());
        ResultSet rs=stat.executeQuery();
        SetResult sr=new SetResult(rs,sql);
        Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
        stat.close();
        co.close();
        return data[0][0].toString();
    }


    public static java.sql.Date stringToDate(String daty){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try{
            java.sql.Date date=new java.sql.Date(sdf.parse(daty).getTime());
            return date;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Timestamp stringToDateTime(String daty){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd;HH:mm");
        try{
            Timestamp date=new Timestamp(sdf.parse(daty).getTime());
            return date;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String dateTimeUtilToSql(String date){
        String daty=date.split("T")[0];
        String time=date.split("T")[1];
        return daty+";"+time;
    }

    public static String getDaty(int weekOfYear,int dayOfWeek,int year){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR,weekOfYear);
        cal.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        cal.set(Calendar.YEAR,year);
        return sdf.format(cal.getTime()).toString();
    }
    public static int getYear(String daty){
        return Integer.valueOf(daty.split("-")[0]);
    }
    public static int getMois(String daty){
        return Integer.valueOf(daty.split("-")[1]);
    }

    public static String dateAddTime(String date,String time)throws Exception{
        String sql="select date '"+date+"' + time '"+time+"'";
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement(sql);
            rs=stat.executeQuery();
            SetResult sr=new SetResult(rs,sql);
            Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
            retour=data[0][0].toString();
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
        return retour;
    }
    public static String getTimeFromDateTime(String datetime) throws Exception{
        String sql="select '"+datetime+"'::time";
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement(sql);
            rs=stat.executeQuery();
            SetResult sr=new SetResult(rs,sql);
            Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
            retour=data[0][0].toString();
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
        return retour;
    }
    public static float arrondi(float f){
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits ( 2 ) ;
        df.setMinimumFractionDigits ( 2 ) ;
        df.setDecimalSeparatorAlwaysShown ( true ) ;
        String str=df.format(f);
        str=str.replace(",","");
        return Float.parseFloat(str);
    }
    public static String millier(int f){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        BigDecimal bd = new BigDecimal(f);
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        return formatter.format(bd.longValue());
    }
    public static String formatter(float f){
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits ( 2 ) ;
        df.setMinimumFractionDigits ( 2 ) ;
        df.setDecimalSeparatorAlwaysShown ( true ) ;
        String str=df.format(f);
        str=str.replace(","," ");
        return str;

    }
    public static float[] YMD(String date1,String date2)throws Exception{
        Connection co=(new EtablirConnex()).getConnection();
        String sql="SELECT DATE_PART('year', AGE('"+date1+"', '"+date2+"')) AS years, DATE_PART('month', AGE('"+date1+"', '"+date2+"')) AS months,DATE_PART('day', AGE('"+date1+"', '"+date2+"')) AS days";

        PreparedStatement stat=co.prepareStatement(sql);
        System.out.println(stat.toString());
        ResultSet rs=stat.executeQuery();
        SetResult sr=new SetResult(rs,sql);
        Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
        stat.close();
        co.close();
        float[] retour={Float.valueOf(data[0][0].toString()),Float.valueOf(data[0][1].toString()),Float.valueOf(data[0][2].toString())};
        return retour;
    }
    public static float timeToFloat(String time){
        int hour=Integer.valueOf(Base.splity(time,":",0));
        int min=Integer.valueOf(Base.splity(time,":",1));
        return hour+(Float.valueOf(min)/60);
    }
    public static String timeMoinsTime(String a,String b) throws Exception{
        String sql="select time '"+a+"' - time '"+b+"'";
        System.out.println(sql);
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement(sql);
            rs=stat.executeQuery();
            SetResult sr=new SetResult(rs,sql);
            Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
            retour=data[0][0].toString();
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
        return retour;

    }
    public static String splity(String s,String by,int id){
        return s.split(by)[id];
    }
    public static String timePlusTime(String time1,String time2)throws Exception{
        String retour=null;
        int[] tmp1={Integer.valueOf(splity(time1,":",0)),Integer.valueOf(splity(time1,":",1)),Integer.valueOf(splity(time1,":",2))};
        int[] tmp2={Integer.valueOf(splity(time2,":",0)),Integer.valueOf(splity(time2,":",1)),Integer.valueOf(splity(time2,":",2))};
        int[] tmp={0,0,0};

        for(int i=2;i>=0;i--){
            while(tmp[i]!=tmp1[i]){
                tmp2[i]++;
                tmp[i]++;
                if(i==2&tmp2[2]==60){tmp2[2]=0;tmp2[1]++;}
                if((i==2||i==1)&tmp2[1]==60){tmp2[1]=0;tmp2[0]++;}

            }
        }
        String[] t={"","",""};
        for(int i=0;i<3;i++){
            t[i]=Integer.toString(tmp2[i]);
            if(Integer.toString(tmp2[i]).length()==1){
                t[i]="0"+Integer.toString(tmp2[i]);
            }
        }
        return t[0]+":"+t[1]+":"+t[2];
    }
    public static String getDateTimePlusTime(String dateTime,String time) throws Exception{
        String sql="select timestamp '"+dateTime+"' + time '"+time+"'";
        String retour=null;
        Connection co=null;
        PreparedStatement stat=null;
        ResultSet rs=null;
        try{
            co=(new EtablirConnex()).getConnection();
            stat=co.prepareStatement(sql);
            rs=stat.executeQuery();
            SetResult sr=new SetResult(rs,sql);
            Object[][] data=sr.getData(sr.nbrColumn(),sr.nbrData());
            retour=data[0][0].toString();
        }catch(Exception e){
            throw e;
        }finally{
            co.close();
            stat.close();
            rs.close();
        }
        return retour;
    }

    public int getWeekOfYear(String daty){
        Calendar cal=Calendar.getInstance();
        cal.setTime(stringToDate(daty));
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

}	