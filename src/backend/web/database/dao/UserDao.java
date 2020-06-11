package backend.web.database.dao;

import backend.web.entity.User;

import java.util.List;

public interface UserDao {

    //boolean find(String email) throws SQLException;

    //void add(Account account);

    boolean logIn(String login, String password);
    boolean findByUsername(String login);
    List<String> getAllUsers();
    void createUser(String login, String password, int day, int month, int year, String name, String surname, String gender);
    User getAccountByLogin(String login);
}
