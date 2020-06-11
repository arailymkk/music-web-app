package backend.web.command.impl;

import backend.web.command.Command;
import backend.web.database.dao.MusicDao;
import backend.web.service.MusicService;

import javax.servlet.http.HttpServletRequest;

public class AddSongToLibraryCommand implements Command {
    private static final String PARAM_RELEASE_DAY = "day";
    private static final String PARAM_RELEASE_MONTH = "month";
    private static final String PARAM_RELEASE_YEAR = "year";
    private static final String PARAM_SONG_NAME = "songName";
    private static final String PARAM_SINGER = "singer";
    private static final String PARAM_GENRE = "genre";

    MusicService service = new MusicService();
    MusicDao dao = new MusicDao();

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;

        String songName = request.getParameter(PARAM_SONG_NAME);
        String singer = request.getParameter(PARAM_SINGER);
        if(service.checkExistenceOfSongBySinger(songName, singer)) {
            request.setAttribute("errorSongExistsMessage", "Song with given name and singer already exists in the library");
            page = "/jsp/addsong.jsp";
        } else {
            int day = Integer.parseInt(request.getParameter(PARAM_RELEASE_DAY));
            int month = Integer.parseInt(request.getParameter(PARAM_RELEASE_MONTH));
            int year = Integer.parseInt(request.getParameter(PARAM_RELEASE_YEAR));
            String genre = request.getParameter(PARAM_GENRE);

            dao.addSongToLibrary(songName, singer, day, month, year, genre);
            request.getSession().setAttribute("songName", songName);
            request.getSession().setAttribute("singer", singer);
            request.setAttribute("newSongAdded", true);
            page = "/jsp/myaccount.jsp";
        }
        return page;
    }
}
