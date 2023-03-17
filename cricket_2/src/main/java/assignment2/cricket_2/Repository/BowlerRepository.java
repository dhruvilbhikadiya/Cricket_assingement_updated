package assignment2.cricket_2.Repository;


import assignment2.cricket_2.Connection.ConnectMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
@Component
public class BowlerRepository implements BowlerInterface {
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> bowlerCollection = database.getCollection("bowler") ;


    public Document getPlayer(int playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        List<Document> players = bowlerCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        Document bowler=null;
        if(players.size()!=0)
        bowler=(Document)players.get(0);
        return bowler;
    }
}
