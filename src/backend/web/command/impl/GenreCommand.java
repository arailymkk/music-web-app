package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.entity.Song;
import backend.web.service.MusicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GenreCommand implements Command {
    MusicDao dao = new MusicDao();
    MusicService service = new MusicService();

    @Override
    public String execute(HttpServletRequest request) {
        String page = request.getParameter("page");
        String genre = request.getParameter("genre");
        List<Song> songs = dao.getSongsByGenre(genre);
        String login = (String)request.getSession().getAttribute("user");
        if(login != null) {
            List<Boolean> existenceList = service.createExistenceList(request, songs, login);
            request.getSession().setAttribute("existenceList", existenceList);
        }

        request.getSession().setAttribute("user", request.getSession().getAttribute("user"));
        request.getSession().setAttribute("genreSongs", songs);
        return page;
    }
}
