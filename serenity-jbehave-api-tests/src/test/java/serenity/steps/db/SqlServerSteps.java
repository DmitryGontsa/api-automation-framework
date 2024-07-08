package serenity.steps.db;

import org.DmitryGontsa.db.DataBaseUtils;
import org.DmitryGontsa.db.dto.Student;
import org.DmitryGontsa.db.dto.User;
import org.junit.Test;

import java.util.List;

public class SqlServerSteps {

    @Test
    public void verifySqlQueryExecution() {
        final String selectStudentsQuery = "select * from hillel_students";
        final String selectUsersQuery = "select * from [hillel-database].dbo.[User]";
        final List<Student> students = DataBaseUtils.executeRetrieveAsListObjects(selectStudentsQuery, Student.class);
        final List<User> users = DataBaseUtils.executeRetrieveAsListObjects(selectUsersQuery, User.class);
    }
}
