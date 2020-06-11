package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class MusicGenresCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String login = (String)request.getSession().getAttribute("user");
        request.getSession().setAttribute("user", login);
        String page = "/jsp/genres.jsp";
        return page;
    }
}
