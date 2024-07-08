package org.DmitryGontsa.db.dto;

import com.hillel.ua.db.ColumnName;
import lombok.Data;

@Data
public class User {

    @ColumnName(name = "Id")
    private String id;

    @ColumnName(name = "Full_Name")
    private String fullName;

    @ColumnName(name = "Age")
    private String age;

    @ColumnName(name = "Phone_Number")
    private String phoneNumber;

    @ColumnName(name = "Is_Covid")
    private String isCovid;
}
