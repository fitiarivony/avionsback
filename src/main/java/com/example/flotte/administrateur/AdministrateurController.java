package com.example.flotte.administrateur;

import org.springframework.web.bind.annotation.*;
import utils.Connect;
import utils.ErrorResponse;
import utils.Response;
import utils.SuccessResponse;

@RestController
@RequestMapping("admin/")
@CrossOrigin(origins="*",allowedHeaders="*")
public class AdministrateurController {

    @GetMapping("/login")
    public Response selectAll(@RequestParam(value = "identifiant") String identifiant, @RequestParam(value = "mdp") String mdp)throws Exception{
        boolean bool=false;
        Administrateur admin=null;
        try {
            admin=new Administrateur(identifiant,mdp);
            bool=admin.login(new Connect());
            System.out.println(admin.getDateExpiration());
            admin.setToken();

        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","You're not admin");
        }

        return new SuccessResponse(admin);

    }
    @GetMapping("/logout/{idAdmin}")
    public Response logout(@PathVariable("idAdmin") String id)throws Exception{
        Administrateur admin=null;
        try {
            admin=new Administrateur(id);
            admin.logout();

        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","idAdmin"+id+" doesn't exist");
        }

        return new SuccessResponse(admin);

    }
    @PostMapping("/login")
    public Response tongaato(@RequestBody Administrateur admin)throws Exception{
        boolean bool=false;
        try {
            bool=admin.login(new Connect());
            System.out.println(admin.getDateExpiration());
            admin.setToken();

        }catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse("201","You're not admin");
        }

        return new SuccessResponse(admin);

    }
}

