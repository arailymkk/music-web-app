package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.entity.Song;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MySongsCommand implements Command {
    MusicDao dao = new MusicDao();
    @Override
    public String execute(HttpServletRequest request) {
        String login = (String)request.getSession().getAttribute("user");
        request.getSession().setAttribute("user", login);
        List<Song> songs = dao.getSongsByLogin(login);
        request.getSession().setAttribute("favoriteSongs", songs);
        String page = "/jsp/mysongs.jsp";
        return page;
    }
}
