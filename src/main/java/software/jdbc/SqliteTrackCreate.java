package software.jdbc;

import software.Track;
import software.TrackCreate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SqliteTrackCreate implements TrackCreate {
    private static final String SQL = "SELECT tracks.Name as track, tracks.Composer as composer,  tracks.Milliseconds as milliseconds,  albums.Title as album, genres.Name as genre, artists.Name as artist FROM tracks, albums, genres, artists WHERE tracks.AlbumId = albums.AlbumId AND albums.ArtistId = artists.ArtistId AND tracks.GenreId = genres.GenreId AND track = ?";
    private final Connection connection;
    private final String trackName;

    public SqliteTrackCreate(String track, Connection connection) {
        this.trackName = track;
        this.connection = connection;
    }

    @Override
    public Track create() {
        try {
            return create(queryAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Track create(ResultSet resultSet) throws SQLException {
        return new Track(
                resultSet.getString("track"),
                resultSet.getString("composer"),
                resultSet.getString("album"),
                resultSet.getString("artist"),
                resultSet.getString("genre"),
                resultSet.getInt("milliseconds")/1000
        );
    }

    private ResultSet queryAll() throws SQLException {
        PreparedStatement preparedStatement = this.prepareStatement();
        return preparedStatement
                .executeQuery();
    }

    private PreparedStatement prepareStatement() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, trackName);
        return preparedStatement;
    }
}
