package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class ChooseLoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/login.jsp";
        return page;
    }
}
