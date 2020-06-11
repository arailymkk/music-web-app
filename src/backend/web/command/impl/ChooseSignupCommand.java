package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class ChooseSignupCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/signup.jsp";
        return page;
    }
}
