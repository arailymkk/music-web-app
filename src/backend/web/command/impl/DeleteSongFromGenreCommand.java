package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.entity.Song;
import backend.web.service.MusicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DeleteSongFromGenreCommand implements Command {
    MusicDao dao = new MusicDao();
    MusicService service = new MusicService();

    @Override
    public String execute(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String page = request.getParameter("page");
        String genre = request.getParameter("genre");
        dao.deleteSongFromLibrary(id);
        dao.deleteSongFromUsers(id);

        if(genre.equals("none")) {
            request.getSession().removeAttribute("songs");
            List<Song> songs = dao.getAllSongs();
            request.getSession().setAttribute("songs", songs);
        }
        else {
            request.getSession().removeAttribute("genreSongs");
            List<Song> genreSongs = dao.getSongsByGenre(genre);
            request.getSession().setAttribute("genreSongs", genreSongs);
        }

        return page;
    }
}