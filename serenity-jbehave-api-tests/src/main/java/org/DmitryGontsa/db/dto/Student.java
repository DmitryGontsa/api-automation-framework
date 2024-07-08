package org.DmitryGontsa.db.dto;

import com.hillel.ua.db.ColumnName;
import lombok.Data;

@Data
public class Student {

    @ColumnName(name = "PersonID")
    private String personId;

    @ColumnName(name = "FirstName")
    private String firstName;

    @ColumnName(name = "LastName")
    private String lastName;

    @ColumnName(name = "Age")
    private String age;

    @ColumnName(name = "City")
    private String city;

    @ColumnName(name = "Address")
    private String address;
}
