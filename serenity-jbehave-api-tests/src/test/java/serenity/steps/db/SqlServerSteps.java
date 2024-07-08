package com.hillel.ua.serenity.steps.db;

import com.hillel.ua.db.DataBaseUtils;
import com.hillel.ua.db.dto.Student;
import com.hillel.ua.db.dto.User;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SqlServerSteps {

    @Test
    public void verifySqlQueryExecution() {
        final String selectStudentsQuery = "select * from hillel_students";
        final String selectUsersQuery = "select * from [hillel-database].dbo.[User]";
        final List<Student> students = DataBaseUtils.executeRetrieveAsListObjects(selectStudentsQuery, Student.class);
        final List<User> users = DataBaseUtils.executeRetrieveAsListObjects(selectUsersQuery, User.class);
    }
}
