package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = "/index.jsp";
        request.getSession().invalidate();
        return page;
    }
}
