package assignment2.cricket_2.Repository;

import assignment2.cricket_2.Connection.ConnectMongo;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
@Component
public class BatsmanRepository implements BatsmanInterface{
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> batsmanCollection = database.getCollection("batsman") ;


    public Document getPlayer(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        Document batsman=(Document)players.get(0);
        return batsman;
    }



}
