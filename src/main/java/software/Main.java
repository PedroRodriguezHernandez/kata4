package software;

import software.jdbc.SqliteAlbumLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        try(Connection connection = DriverManager.getConnection(urlOf("chinook.db"))){
            AlbumLoader albumLoader = new SqliteAlbumLoader(connection);
            List<Album> albums = albumLoader.load();
            for (Album album : albums) {
                System.out.println(album.toString());
            }
        }
    }

    private static String urlOf(String filename) {
        return "jdbc:sqlite:"+filename;
    }
}
