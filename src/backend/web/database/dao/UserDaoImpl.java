package backend.web.database.dao;

import backend.web.database.connectionpool.ConnectionPool;
import backend.web.entity.Gender;
import backend.web.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final String SELECT_LOGIN_ACCOUNT = "Select login, password From userinfo where login = ? and password = ?";
    private static final String SELECT_LOGIN = "Select login From userinfo where login = ?";
    public static final String CREATE_USER = "INSERT INTO userinfo(login, password, birthday, username, surname, gender) VALUES  (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USERS= "Select login From userinfo";
    private static final String SELECT_ACCOUNT_BY_LOGIN = "Select username, surname, birthday, gender from userinfo where login = ?";
    private Connection connection;

    @Override
    public boolean logIn(String login, String password) {
        boolean userLoginCombinationExists = false;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN_ACCOUNT)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                userLoginCombinationExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return userLoginCombinationExists;
    }

    @Override
    public boolean findByUsername(String login) {
        boolean userExists = false;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return userExists;
    }



    public void createUser(String login, String password, int day, int month, int year, String name, String surname, String gender) {
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement statement = connection.prepareStatement(CREATE_USER)) {
            String stringDate = year + "-" + month + "-" + day;
            java.sql.Date date = java.sql.Date.valueOf(stringDate);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setDate(3, date);
            statement.setString(4, name);
            statement.setString(5, surname);
            statement.setString(6, gender);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
    }

    @Override
    public User getAccountByLogin(String login) {
        User user = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                user = new User();
                user.setName(resultSet.getString(1));
                user.setSurname(resultSet.getString(2));
                user.setBirthday(resultSet.getDate(3));
                user.setGender(Gender.valueOf(resultSet.getString(4).toUpperCase()));
                user.setLogin(login);
                user.setBirthday(resultSet.getDate(3));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return user;
    }

    @Override
    public List<String> getAllUsers() {
        ArrayList<String> users = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);
            if(resultSet != null) {
                users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return users;
    }
}
