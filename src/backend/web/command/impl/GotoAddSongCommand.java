package backend.web.command.impl;

import backend.web.command.Command;

import javax.servlet.http.HttpServletRequest;

public class GotoAddSongCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/addsong.jsp";
        return page;
    }
}
