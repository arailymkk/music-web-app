package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    private UserService service = new UserService();
    private MusicDao dao = new MusicDao();
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;

        String login = request.getParameter(PARAM_NAME_LOGIN);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        if(service.checkLogin(login, password)) {
//            List<Song> songs = dao.getSongsByLogin(login);
//            request.getSession().setAttribute("favoriteSongs", songs);
            request.getSession().setAttribute("user", login);
            page = "/jsp/welcome.jsp";
        } else {
            request.setAttribute("errorLoginPassMessage", "Incorrect login or password");
            page = "/jsp/login.jsp";
        }
        return page;
    }
}
