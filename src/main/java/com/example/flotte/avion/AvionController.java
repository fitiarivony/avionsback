package com.example.flotte.avion;

import com.example.flotte.avion.Avion;
import com.example.flotte.administrateur.Administrateur;
import com.example.flotte.assurance.Assurance_avion_detail;
import com.example.flotte.kilometrage.Kilometrage;
import dao.GenericDAO;
import exception.OutOfConnectionException;
import org.springframework.web.bind.annotation.*;
import utils.Connect;
import utils.ErrorResponse;
import utils.Response;
import utils.SuccessResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;

@RestController
@CrossOrigin(origins="*",allowedHeaders="*")
@RequestMapping(path="avions")
public class AvionController {
    @GetMapping
    public Response getAvions() {
      System.out.println("attttt");
        Object[]avion=new Object[0];
        try {
            avion= GenericDAO.selectAll(new Avion(),new Connect()).toArray();
            
        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

    @PostMapping
    public Response registerNewAvion(@RequestBody Avion avion){
        try{

            GenericDAO.save(avion,new Connect());
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

    @DeleteMapping("{idAvion}")
    public Response deleteAvion(@PathVariable("idAvion") String id){
        Avion v=new Avion();
        v.setIdAvion(id);
        try {
            GenericDAO.delete(v,new Connect());
        }catch (Exception e){
            e.printStackTrace();
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(v);
    }

    @PutMapping("{idAvion}")
    public Response updateAvion(@PathVariable("idAvion") String id,@RequestBody Avion avion){
        avion.setIdAvion(id);
        try{
            GenericDAO.update(avion, new Connect());
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }
    
      @PutMapping("{idAvion}/photo")
    public Response updatePhotoAvion(@PathVariable("idAvion") String id,@RequestBody String newphoto){
        Avion avion=new Avion();
        avion.setIdAvion(id);

        try{
            avion=(Avion) GenericDAO.getById(avion,new Connect()).get(0);
            System.out.println("helloooooooooo");
            avion.updatePhoto(newphoto);
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

    @GetMapping("/{idavion}/kilometrages")
    public Response getKilometrage(@PathVariable("idavion") String id,@RequestParam(value="authorization",defaultValue="token")String token) {
        Kilometrage[]kilo=new Kilometrage[0];
        ArrayList<Object>liste=new ArrayList<>();
        Connect conn=null;
        Administrateur admin=new Administrateur();

        try {
            conn=new Connect();
            conn.setuses(true);
            admin.setToken(token);
            admin=admin.checkConnex(conn);


            Avion v=new Avion();
            v.setIdAvion(id);
            kilo=v.listKilometrage(conn);
            Avion_detail ad=new Avion_detail();
            ad.setIdAvion(id);
            ad=(Avion_detail)GenericDAO.getByIds(v,new Connect())[0];
            v=(Avion)GenericDAO.getById(v, conn).get(0);
            liste.add(ad);
            liste.add(kilo);
        }catch(OutOfConnectionException e) {

            return new ErrorResponse(String.valueOf(e.getCode()),e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201",e.getMessage());
        }finally {
            try {
                conn.forceClose();
            } catch (SQLException e) {
                //e.printStackTrace();
                return new ErrorResponse("201",e.getMessage());
            }
        }
        return new SuccessResponse(liste.toArray());
    }
    
    @GetMapping("/{idavion}/kilometrage")
    public Response getKilo(@PathVariable("idavion") String id,@RequestHeader(HttpHeaders.AUTHORIZATION)String token) {
        Kilometrage[]kilo=new Kilometrage[0];
        ArrayList<Object>liste=new ArrayList<>();
        Connect conn=null;
        Administrateur admin=new Administrateur();

        try {
            conn=new Connect();
            conn.setuses(true);
            admin.setToken(token);
            admin=admin.checkConnex(conn);


            Avion v=new Avion();
            v.setIdAvion(id);
            kilo=v.listKilometrage(conn);
            Avion_detail ad=new Avion_detail();
            ad.setIdAvion(id);
            ad=(Avion_detail)GenericDAO.getByIds(ad,new Connect())[0];
            v=(Avion)GenericDAO.getById(v, conn).get(0);
            liste.add(ad);
            liste.add(kilo);
        }catch(OutOfConnectionException e) {

            return new ErrorResponse(String.valueOf(e.getCode()),e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201",e.getMessage());
        }finally {
            try {
                conn.forceClose();
            } catch (SQLException e) {
                //e.printStackTrace();
                return new ErrorResponse("201",e.getMessage());
            }
        }
        return new SuccessResponse(liste.toArray());
    }
      @GetMapping("expiration/{mois}")
    public Response getAssuranceExpire(@PathVariable(value="mois") int mois) {
    	Connect conn=null;
    	try {
    		conn=new Connect();
    		Assurance_avion_detail ass=new Assurance_avion_detail();
    		return new SuccessResponse(ass.getExpiration(mois, conn));	
    	}catch(Exception e) {
    		e.printStackTrace();
   		 return new ErrorResponse("201",e.getMessage());
    	}finally {
    		try {
				conn.forceClose();
			} catch (SQLException e) {
				//e.printStackTrace();
				return new ErrorResponse("201",e.getMessage());
			}
    	}
    }
}
