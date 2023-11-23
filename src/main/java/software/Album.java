package software;

import java.util.ArrayList;
import java.util.List;

public record Album (String name, String artist, List<Track> tracks){
    @Override
    public String toString() {
        return "Album:" + name + ", artist='" + artist + '\'' + ", tracks=" + printTracksName();
    }

    private String printTracksName() {
        List<String> result = new ArrayList<>();
        for (Track track : tracks) {
            result.add(track.name());
        }
        return result.toString();
    }
}
