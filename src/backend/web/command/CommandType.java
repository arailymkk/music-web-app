package backend.web.command;

import backend.web.command.impl.*;
import backend.web.command.impl.*;

public enum  CommandType {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    ISLOGIN(new ChooseLoginCommand()),
    ISSIGNUP(new ChooseSignupCommand()),
    SIGNUP(new SignupCommand()),
    RETURN(new ReturnCommand()),
    MUSICMAIN(new MusicMainCommand()),
    MYSONGS(new MySongsCommand()),
    ADDSONG(new AddSongCommand()),
    MUSICGENRES(new MusicGenresCommand()),
    REMOVESONG(new RemoveSongCommand()),
    GENRE(new GenreCommand()),
    DELETESONGFROMGENRE(new DeleteSongFromGenreCommand()),
    GETACCOUNT(new GetAccountCommand()),
    ADD(new AddSongToLibraryCommand()),
    GOTOADDSONG(new GotoAddSongCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
