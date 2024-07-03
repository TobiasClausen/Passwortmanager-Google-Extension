package org.example.API;

import org.example.DTOs.DtoOutputPassword;
import org.example.DTOs.DtoPasswordsFromDB;
import org.example.Database.DALTablePassword;
import org.example.Encryption.Encryption;
import org.example.PasswordGenerator.PasswordGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PasswordControllerAPI {
    private static final int passwordlaenge = 20;
    Encryption encryption;
    DALTablePassword dalTablePassword;
    public PasswordControllerAPI(){
        dalTablePassword = new DALTablePassword();
        encryption = new Encryption();
    }
    @GetMapping("/getPassword")
    public List<DtoOutputPassword> getPassword(@RequestParam String input) {
        System.out.println("getPassword");

        List<DtoOutputPassword> dtos = new ArrayList<>();
        List<DtoPasswordsFromDB> dtoPasswordsFromDBs = new ArrayList<>();
        dtoPasswordsFromDBs=dalTablePassword.getSpecificPassword(input);
        for (int i = 0; i<dtoPasswordsFromDBs.size(); i++){
            DtoOutputPassword dtoOutputPassword = new DtoOutputPassword();
            dtoOutputPassword.setPasswords(encryption.getDecryption(dtoPasswordsFromDBs.get(i).getPasswords()));
            dtoOutputPassword.setDescription(dtoPasswordsFromDBs.get(i).getDescription());
            dtoOutputPassword.setDate(dtoPasswordsFromDBs.get(i).getDate());
            dtos.add(dtoOutputPassword);
        }


        return dtos;
    }

    @PostMapping("/setPassword")
    public String setPassword(@RequestParam String newPassword, @RequestParam String input) {
        System.out.println("setPassword");
        dalTablePassword.writeinDB(encryption.setEncryption(newPassword), input);
        return "Password updated successfully";
    }
    @PostMapping("/createPassword")
    public String createPassword(@RequestParam String input) {
        System.out.println("CreatePassword");
        DALTablePassword dalTablePassword = new DALTablePassword();

        dalTablePassword.writeinDB(encryption.setEncryption(PasswordGenerator.createPassword(passwordlaenge)), input);

        return "Password updated successfully";
    }
}

