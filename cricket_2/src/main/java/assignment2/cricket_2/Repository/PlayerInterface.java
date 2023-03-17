package assignment2.cricket_2.Repository;

import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerInterface {
    public Document getPlayer(int playerId);
}
