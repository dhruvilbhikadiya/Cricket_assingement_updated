package assignment2.cricket_2.Repository;

import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface BowlerInterface {
    public Document getPlayer(int playerId, int matchId);
}
