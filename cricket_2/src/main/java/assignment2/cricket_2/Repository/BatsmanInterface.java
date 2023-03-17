package assignment2.cricket_2.Repository;

import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface BatsmanInterface {
    public Document getPlayer(Integer playerId, int matchId);
}
