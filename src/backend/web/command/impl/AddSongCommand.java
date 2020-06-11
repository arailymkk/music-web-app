package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AddSongCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String page = request.getParameter("page");
        String login = (String)request.getSession().getAttribute("user");

        String id = request.getParameter("id");
        int i = Integer.parseInt(request.getParameter("increment"));
        MusicDao dao = new MusicDao();
        dao.addSongToUser(login, Integer.parseInt(id));

        ((List<Boolean>) request.getSession().getAttribute("existenceList")).set(i, true);
        return page;
    }
}
