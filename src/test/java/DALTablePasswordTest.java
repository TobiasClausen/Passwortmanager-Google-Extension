package org.example.Database;

import org.example.DTOs.DtoPasswordsFromDB;
import org.example.Database.DALTablePassword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DALTablePasswordTest {
    DALTablePassword testee = new DALTablePassword();
    @Test
    public void getPassword() throws SQLException {
        List<DtoPasswordsFromDB> dtos = new ArrayList<>();
        dtos = testee.getPasswords();

        Assertions.assertNotNull(dtos.get(0).getPasswords());
    }
    @Test
    public void getSpecificPassword() throws SQLException {
        List<DtoPasswordsFromDB> dtos = new ArrayList<>();
        dtos = testee.getPasswords();


        dtos=testee.getSpecificPassword(dtos.get(0).getDescription());

        Assertions.assertNotNull(dtos);
    }
}
