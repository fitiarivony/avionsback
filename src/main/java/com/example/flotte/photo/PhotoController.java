package com.example.flotte.photo;

import com.example.flotte.avion.Avion;
import com.example.flotte.administrateur.Administrateur;
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

@RestController
@CrossOrigin(origins="*",allowedHeaders="*")
@RequestMapping(path="photo")
public class PhotoController {
    @GetMapping
    public Response getAvions() {
        Object[]avion=new Object[0];
        try {
            avion= GenericDAO.selectAll(new Photo(),new Connect()).toArray();
        }catch(Exception e) {
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

    @PostMapping
    public Response registerNewAvion(@RequestBody Photo avion){
        try{
            System.out.println(avion);
            GenericDAO.save(avion,new Connect());
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

    @DeleteMapping("{idAvion}")
    public Response deleteAvion(@PathVariable("idAvion") String id){
        Photo v=new Photo();
        v.setIdphoto(Integer.parseInt(id));
        try {
            GenericDAO.delete(v,new Connect());
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(v);
    }

    @PutMapping("{idAvion}")
    public Response updateAvion(@PathVariable("idAvion") String id,@RequestBody Photo avion){
        avion.setIdphoto(Integer.parseInt(id));
        try{
            GenericDAO.update(avion, new Connect());
        }catch (Exception e){
            return new ErrorResponse("201",e.getMessage());
        }
        return new SuccessResponse(avion);
    }

   
}
