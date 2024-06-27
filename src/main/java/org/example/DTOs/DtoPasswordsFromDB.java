package org.example.DTOs;


public class DtoPasswordsFromDB {
    private byte[] passwords;
    private String description;
    private String date;

    public byte[] getPasswords() {
        return passwords;
    }

    public void setPasswords(byte[] passwords) {
        this.passwords = passwords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

