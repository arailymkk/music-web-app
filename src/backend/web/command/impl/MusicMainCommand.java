package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.entity.Song;
import backend.web.service.MusicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MusicMainCommand implements Command {
    MusicDao dao = new MusicDao();
    MusicService service = new MusicService();

    @Override
    public String execute(HttpServletRequest request) {
        String page = "/jsp/musicMain.jsp";
        List<Song> songs = dao.getAllSongs();
        List<Song> latestSongs = service.getTopLatestReleasedSongs();
        String login = (String)request.getSession().getAttribute("user");
        if(login != null) {
            List<Boolean> existenceList = service.createExistenceList(request, songs, login);
            request.getSession().setAttribute("existenceList", existenceList);
//            List<Boolean> latestReleaseExistenceList = service.createExistenceList(request, latestSongs, login);
//            request.getSession().setAttribute("latestReleaseExistenceList", latestReleaseExistenceList);
        }
        request.getSession().setAttribute("user", request.getSession().getAttribute("user"));
        request.getSession().setAttribute("songs", songs);
        request.getSession().setAttribute("latestSongs", latestSongs);
        return page;
    }
}
