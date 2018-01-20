package utilities.database;

import core.javafx.menu.MenuController;
import utilities.enums.AlertType;

import java.io.File;
import java.io.FileInputStream;
import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;

@SuppressWarnings("all")
public class Database {

    private static String server;
    private static String username;
    private static String password;
    private static Connection connection;

    private static String connectionError = "Could not connect to database.";

    public static Connection getConnection() {
        FileInputStream fileInput;
        try {
            if (server == null || username == null || password == null) {
                fileInput = new FileInputStream("Client/src/utilities/database/DatabaseCredentials.properties");

                Properties properties = new Properties();
                properties.load(fileInput);
                fileInput.close();

                server = properties.getProperty("server");
                username = properties.getProperty("username");
                password = properties.getProperty("password");
            }


            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://" + server + ":3306/MyAuctions", username, password);
            }
            return connection;
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            return null;
        }
    }

    public static ResultSet getData(final String query, final String[] values) throws ConnectException, SQLException {
        PreparedStatement preparedStatement;
        final Connection connection = Database.getConnection();
        if (connection != null) {
            preparedStatement = connection.prepareStatement(query);
        } else {
            throw new ConnectException(connectionError);
        }

        try {
            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    final int index = i + 1;

                    fillPreparedStatementRowWithValue(preparedStatement, values[i], index);
                }
            }
            return preparedStatement.executeQuery();
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return null;
    }

    public static ResultSet getDataForSearchTerm(final String query, final String searchTerm) {
        PreparedStatement preparedStatement;
        try {
            final Connection connection = Database.getConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement(query);
            } else {
                throw new ConnectException(connectionError);
            }


            preparedStatement.setString(1, "%" + searchTerm + "%");

            return preparedStatement.executeQuery();
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return null;
    }

    public static int setData(final String query, final String[] values, final boolean isUpdateQuery) {
        int updateCount = -1;
        PreparedStatement preparedStatement;

        try {
            final Connection connection = Database.getConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement(query);
            } else {
                throw new ConnectException(connectionError);
            }

            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    final int index = i + 1;

                    fillPreparedStatementRowWithValue(preparedStatement, values[i], index);
                }
            }

            if (isUpdateQuery) preparedStatement.executeUpdate();
            else preparedStatement.executeQuery();

            updateCount = preparedStatement.getUpdateCount();
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return updateCount;
    }

    /**
     * @param query:         This is the query to execute. Make sure to write it in such a way that all the normal values can be parsed first, then afterwards the
     *                       images will be parsed, all happening in the order that the arrays are in.
     * @param values:        This is the array of normal values, like the the getData() method
     * @param images:        This is an array of images
     * @param isUpdateQuery: This indicates if the query is an update query
     */
    public static int setDataWithImages(final String query, final String[] values, final File[] images, final boolean isUpdateQuery) {
        int updateCount = -1;
        PreparedStatement preparedStatement;

        try {
            final Connection connection = Database.getConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement(query);
            } else {
                throw new ConnectException(connectionError);
            }

            int index = 0;

            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    index += 1;

                    fillPreparedStatementRowWithValue(preparedStatement, values[i], index);
                }
            }
            if (images != null && images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    index += 1;
                    final FileInputStream fileInputStream = new FileInputStream(images[i]);

                    preparedStatement.setBinaryStream(index, fileInputStream, (int) images[i].length());
                }
            }

            if (isUpdateQuery) preparedStatement.executeUpdate();
            else preparedStatement.executeQuery();

            updateCount = preparedStatement.getUpdateCount();
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return updateCount;
    }

    /**
     * This method is used to set the Profile Picture of a user.
     * It is needed to have this as a seperate method because the structure of this query is different;
     * The value of the image has to be set before the userId, the other setData methods do this in the opposite sequence.
     *
     * @param userId
     * @param image
     * @return
     */
    public static int setProfilePicture(final int userId, final File image) {
        int updateCount = -1;
        PreparedStatement preparedStatement;

        try {
            final Connection connection = Database.getConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement("UPDATE Account SET image = ? WHERE id = ?");
            } else {
                throw new ConnectException(connectionError);
            }

            // Add the image to the PreparedStatement
            final FileInputStream fileInputStream = new FileInputStream(image);
            preparedStatement.setBinaryStream(1, fileInputStream, (int) image.length());

            // Add the userId to the PreparedStatement
            fillPreparedStatementRowWithValue(preparedStatement, String.valueOf(userId), 2);

            preparedStatement.executeUpdate();
            updateCount = preparedStatement.getUpdateCount();
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return updateCount;
    }

    /**
     * @param query: Usage for query --> {call increase_salaries_for_department(?, ?)}
     *               increase_salaries_for_department being the name of the stored procedure
     */
    public static int executeStoredProcedure(final String query, final String[] values) {
        int updateCount = -1;
        CallableStatement callableStatement;

        try {
            connection = Database.getConnection();
            if (connection != null) {
                callableStatement = connection.prepareCall(query);
            } else {
                throw new ConnectException(connectionError);
            }

            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    final int index = i + 1;

                    if (isDouble(values[i])) {
                        callableStatement.setDouble(index, Double.parseDouble(values[i]));
                    } else if (isInteger(values[i])) {
                        callableStatement.setInt(index, Integer.parseInt(values[i]));
                    } else if (isDate(values[i])) {
                        callableStatement.setTimestamp(index, Timestamp.valueOf(LocalDateTime.parse(values[i])));
                    } else if (isBoolean(values[i])) {
                        callableStatement.setBoolean(index, Boolean.parseBoolean(values[i]));
                    } else {
                        callableStatement.setString(index, values[i]);
                    }
                }
            }

            callableStatement.execute();
            updateCount = callableStatement.getUpdateCount();
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
        return updateCount;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            }
        }
    }

    public static boolean isDouble(final String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private static void fillPreparedStatementRowWithValue(final PreparedStatement preparedStatement, final String value, final int index) throws SQLException {
        if (isDouble(value)) {
            preparedStatement.setDouble(index, Double.parseDouble(value));
        } else if (isInteger(value)) {
            preparedStatement.setInt(index, Integer.parseInt(value));
        } else if (isDate(value)) {
            preparedStatement.setTimestamp(index, Timestamp.valueOf(LocalDateTime.parse(value)));
        } else if (isBoolean(value)) {
            preparedStatement.setBoolean(index, Boolean.parseBoolean(value));
        } else {
            preparedStatement.setString(index, value);
        }
    }

    private static boolean isBoolean(final String paramValue) {
        final String value = paramValue.toLowerCase();

        return value.equals("true") || value.equals("false");
    }

    private static boolean isInteger(final String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private static boolean isDate(final String value) {
        try {
            LocalDateTime.parse(value);
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}
