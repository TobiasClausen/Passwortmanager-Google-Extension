package org.example;

import org.example.DTOs.DtoPasswordsFromDB;
import org.example.Database.DALTablePassword;
import org.example.Encryption.Encryption;
import org.example.PasswordGenerator.PasswordGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PasswordController {

    private String storedPassword = "mySecretPassword";
    private static final int passwordlaenge = 20;

    @GetMapping("/getPassword")
    public List<DtoPasswordsFromDB> getPassword(@RequestParam String input) {
        DALTablePassword dalTablePassword = new DALTablePassword();
        Encryption encryption = new Encryption();

        List<DtoPasswordsFromDB> dtos = new ArrayList<>();

        return dalTablePassword.getSpecificPassword(input, dtos);
    }

    @PostMapping("/setPassword")
    public String setPassword(@RequestParam String newPassword, String input) {
        DALTablePassword dalTablePassword = new DALTablePassword();
        Encryption encryption = new Encryption();
        dalTablePassword.writeinDB(encryption.setEncryption(newPassword), input);
        return "Password updated successfully";
    }
    @PostMapping("/createPassword")
    public String createPassword(@RequestParam String input) {
        DALTablePassword dalTablePassword = new DALTablePassword();
        Encryption encryption = new Encryption();

        dalTablePassword.writeinDB(encryption.setEncryption(PasswordGenerator.createPassword(passwordlaenge)), input);

        return "Password updated successfully";
    }
}

