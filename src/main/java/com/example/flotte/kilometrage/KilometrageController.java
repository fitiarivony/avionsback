package com.example.flotte.kilometrage;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import utils.*;

@RestController
@RequestMapping("kilometrage")
@CrossOrigin(origins="*",allowedHeaders="*")
public class KilometrageController {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        String test=new String();
        try {
            Connect conn=new Connect();
            test=conn.toString();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }

        return String.format("Hello %s! \n %s  ", name,test);
    }

    @PostMapping("")
    public Response insert(@RequestBody Kilometrage kilo) throws Exception{

        try {
            Connect conn=new Connect();
            kilo.insert(conn);
            

        } catch(Exception ex) {
            ex.printStackTrace();
            return new ErrorResponse("201","Client insertion error.");
        }

        return new SuccessResponse(kilo);
    }


    @GetMapping("")
    public Response selectAll()throws Exception{
        Kilometrage[]kilos=null;
        Kilometrage kilo=new Kilometrage();
        try {
            kilos=kilo.selectAll(new Connect());
        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","Client listing error.");
        }

        return new SuccessResponse(kilos);

    }
    @DeleteMapping(value="/{id}")
    public Response delete_km(@PathVariable(value = "id") String paramId) throws SQLException{

        Kilometrage k = new Kilometrage();
        k.setIdkilometrage(paramId);
        try {
            k.delete(new Connect());

        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","Kilometrage deleting error.");
        }
        return new SuccessResponse(k);
    }

    @PutMapping("")
    public Response update_km(@RequestBody Kilometrage k) throws SQLException{

        try {
            k.update(new Connect());
        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","Kilometrage updating error");
        }
        return new SuccessResponse(k);
    }
}
