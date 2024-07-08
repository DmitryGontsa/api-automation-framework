package org.DmitryGontsa.db;

import org.DmitryGontsa.common.PropertiesReader;
import org.DmitryGontsa.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public class DataBaseUtils {

    private static Connection connection;

    private DataBaseUtils() {
    }

    private static void initConnection() {

        final PropertiesReader propertiesReader = new PropertiesReader();

        final String userName = propertiesReader.getProperty("sql.server.username");
        final String password = propertiesReader.getProperty("sql.server.password");
        final String connectionUrl = String.format(propertiesReader.getProperty("sql.server.connection.string"), userName, password);

        try {
            Logger.out.debug("Trying init connection to the SQL Server");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionUrl);
            Logger.out.debug("Connection has successfully established!");
        } catch (final SQLException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to connect to the sever!", e);
        }
    }

    public static void executeQuery(final String query) {
        try {
            final Statement statement = tryToConnect();
            Logger.out.debug(String.format("Connection is established. Executing following query [%s]!", query));
            statement.executeUpdate(query);
            Logger.out.debug(String.format("Following query [%s] is successfully executed!", query));
        } catch (final SQLException e) {
            throw new IllegalStateException("Unable to execute query!", e);
        }
    }

    public static <T> List<T> executeRetrieveAsListObjects(final String query, final Class<T> returnType) {//

        final List<Map<String, String>> results = executeRetrieve(query);

        final List<T> records = new ArrayList<>();

        try {
            for (final Map<String, String> row : results) {

                final T instance = returnType.getDeclaredConstructor().newInstance();

                final List<Field> fields = Arrays.asList(instance.getClass().getDeclaredFields());

                fields.forEach(field -> {
                    final String objectFieldName = field.getAnnotation(ColumnName.class).name();

                    if (row.containsKey(objectFieldName)) {

                        field.setAccessible(true);

                        try {
                            final String dbColumnValue = row.get(objectFieldName);
                            field.set(instance, dbColumnValue);
                        } catch (final IllegalAccessException e) {
                            throw new IllegalStateException("An Exception occurred!", e);
                        }
                    }
                });
                records.add(instance);
            }
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
                       NoSuchMethodException e) {
            throw new IllegalStateException("An Exception occurred!", e);
        }
        return records;
    }

    public static List<Map<String, String>> executeRetrieve(final String query) {
        final List<Map<String, String>> rowsData = new ArrayList<>();
        try {
            final Statement statement = tryToConnect();
            final ResultSet resultSet = statement.executeQuery(query);
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                final Map<String, String> columnData = new HashMap<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    final String columnName = resultSetMetaData.getColumnName(columnIndex);
                    final String columnValue = resultSet.getString(columnIndex);
                    columnData.put(columnName, columnValue);
                }
                rowsData.add(columnData);
            }
        } catch (final SQLException e) {
            throw new IllegalStateException("Unable to execute query!", e);
        }
        return rowsData;
    }

    private static Statement tryToConnect() throws SQLException {
        if (Objects.isNull(connection) || connection.isClosed()) {
            Logger.out.debug("Connection is closed. Perform reconnecting!");
            initConnection();
        }
        return connection.createStatement();
    }
}
