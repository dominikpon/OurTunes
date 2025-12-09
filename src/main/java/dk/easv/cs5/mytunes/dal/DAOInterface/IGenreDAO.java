package dk.easv.cs5.mytunes.dal.DAOInterface;

import dk.easv.cs5.mytunes.be.Genre;

import java.sql.SQLException;
import java.util.List;

public interface IGenreDAO  {
    public void save(Genre genre) throws SQLException;

    List<Genre> getAllGenres();
}
