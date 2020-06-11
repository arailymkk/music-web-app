package backend.web.service;

import backend.web.database.dao.MusicDao;
import backend.web.entity.Song;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicService {
    private MusicDao dao = new MusicDao();

    public boolean checkExistenceOfSong(String login, int id) {
        return checkExistence(dao.getSongsAsList(login), id);
    }

    public boolean checkExistenceOfSongBySinger(String song, String singer) {
        return dao.getSongSingerPair(song, singer);
    }

    private boolean checkExistence(String songsList, int id) {
        if(songsList == null || songsList.length() == 0) {
            return false;
        }
        String[] parsedIds = parseSongsId(songsList);
        for(String songId: parsedIds) {
            if(Integer.parseInt(songId)==id) {
                return true;
            }
        }
        return false;
    }

    private String[] parseSongsId(String songsList) {
        String [] ids = songsList.split(",");
        return ids;
    }

    public List<Boolean> createExistenceList(HttpServletRequest request, List<Song> songs, String login) {
        List<Boolean> existenceList = new ArrayList<>();

        for (Song song : songs) {
            if (checkExistenceOfSong(login, song.getId())) {
                existenceList.add(true);
            } else {
                existenceList.add(false);
            }
        }
        request.getSession().setAttribute("existenceList", existenceList);
        return existenceList;
    }

    public List<Song> getTopLatestReleasedSongs() {
        return getSortedSongs().subList(0, 10);
    }

    private List<Song> getSortedSongs(){
        List<Song> songs = dao.getAllSongs();
        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song o1, Song o2) {
                return o2.getReleaseDate().compareTo(o1.getReleaseDate());
            }
        });
        return songs;
    }

}
