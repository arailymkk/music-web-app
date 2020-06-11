package backend.web.database.dao;

import backend.web.database.connectionpool.ConnectionPool;
import backend.web.entity.Genre;
import backend.web.entity.Song;

import java.sql.*;
import java.util.*;

public class MusicDao {

    private static final String SELECT_SONG_BY_ID = "Select songname, singer, genre, releasedate From songs where idsong = ?";
    private static final String SELECT_SONG_BY_GENRE = "Select idsong, songname, singer, releasedate From songs where genre = ?";
    private static final String SELECT_SONGS = "Select * From songs";
    private static final String SELECT_SONGS_LIST= "Select songslist From userinfo where login = ?";
    private static final String UPDATE_SONGS_LIST = "Update userinfo SET songslist = ?" + " WHERE login = ?;";
    private static final String DELETE_SONG= "Delete From songs where idsong = ?";
    private static final String SELECT_SONG_SINGER_PAIR = "Select songname, singer From songs where songname = ? and singer = ?";
    public static final String ADD_SONG = "INSERT INTO songs(songname, singer, releasedate, genre) VALUES  (?, ?, ?, ?)";

    private Connection connection;
    private UserDao userDao = new UserDaoImpl();

    public List<Song> getAllSongs() {
        List<Song> songs = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_SONGS);
            if(resultSet != null) {
                songs = new ArrayList<>();
                while (resultSet.next()) {
                    songs.add(findSongById(resultSet.getInt("idsong")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return songs;
    }

    public void addSongToLibrary(String songName, String singer, int day, int month, int year, String genre) {
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement statement = connection.prepareStatement(ADD_SONG)) {
            String stringDate = year + "-" + month + "-" + day;
            java.sql.Date date = java.sql.Date.valueOf(stringDate);
            statement.setString(1, songName);
            statement.setString(2, singer);
            statement.setDate(3, date);
            statement.setString(4, genre);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
    }

    public boolean getSongSingerPair(String song, String singer) {
        boolean pairExists = false;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SONG_SINGER_PAIR)) {
            preparedStatement.setString(1, song);
            preparedStatement.setString(2, singer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                pairExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return pairExists;
    }

    public Song findSongById(int id) {
        Song song = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SONG_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                song = new Song();
                song.setId(id);
                song.setName(resultSet.getString(1));
                song.setSinger(resultSet.getString(2));
                song.setGenre(Genre.valueOf(resultSet.getString(3).toUpperCase()));
                song.setReleaseDate(resultSet.getDate(4));
                return song;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return song;
    }

    public List<Song> getSongsByLogin(String login) {
        List<Song> songs = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SONGS_LIST)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
               String songsList = resultSet.getString(1);
               if(songsList != null && songsList.length() > 0) {
                   songs = new ArrayList<>();
                   String[] parsedSongsList = parseSongsId(songsList);
                   for (String id : parsedSongsList) {
                       Song song = findSongById(Integer.parseInt(id));
                       songs.add(song);
                   }
               }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return songs;
    }

    public void deleteSongFromUser(String login, int songId) {
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SONGS_LIST)) {
            String newSongsList = deleteSongFromList(songId, getSongsAsList(login));
            preparedStatement.setString(1, newSongsList);
            preparedStatement.setString(2, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
    }

    public void addSongToUser(String login, int songId) {
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SONGS_LIST)) {
            String newSongsList = addSongToList(songId, getSongsAsList(login));
            preparedStatement.setString(1, newSongsList);
            preparedStatement.setString(2, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
    }

    public List<Song> getSongsByGenre(String genre){
        List<Song> songs = new ArrayList<>();
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SONG_BY_GENRE)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Song song = new Song();
                song.setId(resultSet.getInt(1));
                song.setName(resultSet.getString(2));
                song.setSinger(resultSet.getString(3));
                song.setGenre(Genre.valueOf(genre.toUpperCase()));
                song.setReleaseDate(resultSet.getDate(4));
                songs.add(song);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return songs;
    }

    public String getSongsAsList(String login){
        String songsList = null;
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SONGS_LIST)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.first()) {
                songsList = resultSet.getString(1);
                //String newSongsList = addSongToList(songId, songsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
        return songsList;
    }

    private String[] parseSongsId(String songsList) {
        String [] ids = songsList.split(",");
        return ids;
    }

    private String deleteSongFromList(int id, String songsList){
        if(songsList == null) return null;
        if(songsList.equals("")) return "";
        String [] parsedList = parseSongsId(songsList);
        String newSongsList = "";
        for(int i = 0; i < parsedList.length; i++) {
            if(Integer.parseInt(parsedList[i]) != id){
                if(newSongsList.length() != 0) {
                    newSongsList += "," + parsedList[i];
                }
                else {
                    newSongsList += parsedList[i];
                }
            }
        }
        return newSongsList;
    }

    private String addSongToList(int id, String songsList) {
        if(songsList == null) {
            songsList = new String();
            return songsList + id;
        }
        if(songsList.equals("")) {
            return "" + id;
        }
        else {
            if(!checkExistence(songsList, id)) {
                return songsList + "," + id;
            }
            else {
                System.out.println("already exists");
                return songsList;
            }
        }
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

    public void deleteSongFromLibrary(int id) {
        connection = ConnectionPool.INSTANCE.takeConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SONG)) {
            preparedStatement.setInt(1, id);
            //deleteSongFromUsers(id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ConnectionPool.INSTANCE.releaseConnection(connection);
            }
        }
    }

    public void deleteSongFromUsers(int id) {
        List<String> users = userDao.getAllUsers();
        if(users != null) {
            for(String login: users) {
                deleteSongFromUser(login, id);
            }
        }
    }

}
