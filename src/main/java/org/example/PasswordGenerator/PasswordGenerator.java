package org.example.PasswordGenerator;


import java.util.Random;

public class PasswordGenerator {

    public static String createPassword(int länge) {
        String zeichenkette = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#*%[]|{}&/()=!$£<>+-?";
        Random zufallszahl = new Random();
        StringBuilder passwort = new StringBuilder(länge);
        for (int i = 0; i < länge; i++) {
            int index = zufallszahl.nextInt(zeichenkette.length());
            passwort.append(zeichenkette.charAt(index));
        }
        return passwort.toString();
    }
}


