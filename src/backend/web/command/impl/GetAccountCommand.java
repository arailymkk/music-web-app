package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.UserDao;
import backend.web.database.dao.UserDaoImpl;
import backend.web.entity.User;

import javax.servlet.http.HttpServletRequest;

public class GetAccountCommand implements Command {
    UserDao dao = new UserDaoImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/myaccount.jsp";
        String login = (String)request.getSession().getAttribute("user");
        User user = dao.getAccountByLogin(login);
        request.getSession().setAttribute("account", user);
        return page;
    }
}
