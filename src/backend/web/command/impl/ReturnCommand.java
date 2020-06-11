package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class ReturnCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/welcome.jsp";
        return page;
    }
}
