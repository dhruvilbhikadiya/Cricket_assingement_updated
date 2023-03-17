package assignment2.cricket_2.Repository;

import assignment2.cricket_2.Connection.ConnectMongo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
@Component
public class PlayerRepository implements PlayerInterface
{
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> playerCollection = database.getCollection("players") ;

    public Document getPlayer(int playerId) {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
         Document player=(Document) players.get(0) ;
         return player;
    }
}
