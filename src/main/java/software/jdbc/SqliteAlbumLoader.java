package software.jdbc;

import software.Album;
import software.AlbumLoader;
import software.Track;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteAlbumLoader implements AlbumLoader {
    private static final String SQL = "SELECT albums.Title as album, artists.Name as artist, group_concat(tracks.Name) as tracks FROM albums, artists, tracks WHERE albums.ArtistId = artists.ArtistId  AND albums.AlbumId = tracks.AlbumId GROUP BY album";
    private final Connection connection;

    public SqliteAlbumLoader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Album> load() {
        try {
            return load(queryAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Album> load(ResultSet resultSet) throws SQLException {
        List<Album> result = new ArrayList<>();
        while (resultSet.next()){
            result.add(albumFrom(resultSet));
        }
        return result;
    }

    private Album albumFrom(ResultSet resultSet) throws SQLException {
        return new Album(
                resultSet.getString("album"),
                resultSet.getString("artist"),
                tracksFrom(resultSet.getString("tracks").split(","))
        );
    }

    private List<Track> tracksFrom(String[] tracks) {
        List<Track> result = new ArrayList<>();
        for (String track : tracks) {
            result.add(new SqliteTrackCreate(track, connection).create());
        }
        return result;
    }

    private ResultSet queryAll() throws SQLException {
        return connection
                .createStatement()
                .executeQuery(SQL);
    }
}
