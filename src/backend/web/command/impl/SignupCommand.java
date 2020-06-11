package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class SignupCommand implements Command {

    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_BIRTH_DAY = "day";
    private static final String PARAM_BIRTH_MONTH = "month";
    private static final String PARAM_BIRTH_YEAR = "year";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_SURNAME = "surname";
    private static final String PARAM_GENDER = "gender";
    private UserService service = new UserService();
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;

        String login = request.getParameter(PARAM_NAME_LOGIN);
        if(service.checkUserExistence(login)) {
            //request.setAttribute("user", login);
            request.getSession().setAttribute("errorSignupPassMessage", "User already exists");
            page = "/jsp/signup.jsp";
        } else {
            String password = request.getParameter(PARAM_NAME_PASSWORD);
            int day = Integer.parseInt(request.getParameter(PARAM_BIRTH_DAY));
            int month = Integer.parseInt(request.getParameter(PARAM_BIRTH_MONTH));
            int year = Integer.parseInt(request.getParameter(PARAM_BIRTH_YEAR));
            String name = request.getParameter(PARAM_NAME);
            String surname = request.getParameter(PARAM_SURNAME);
            String gender = request.getParameter(PARAM_GENDER);

            service.addUser(login, password, day, month, year, name, surname, gender);
            request.setAttribute("newUserNotification", "You have just registered new account");
            request.getSession().setAttribute("user", login);
            request.getSession().setAttribute("username", name);
            request.getSession().setAttribute("surname", surname);
            page = "/jsp/welcome.jsp";
        }
        return page;
    }
}
