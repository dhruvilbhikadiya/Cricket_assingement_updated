package assignment2.cricket_2.service;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.ScoreBoardInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreBoardService {
    @Autowired
    private final ScoreBoardInterface scoreBoardRepository;
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> scoreBoardCollection = database.getCollection("scoreboards");
    public ResponseEntity<Document> getScoreBoardOfMatch(@PathVariable("matchId") int matchId)
    {
        Document scoreBoard= getScoreBoard(matchId);
        if(scoreBoard!=null)
        {

            return ResponseEntity.accepted().body(scoreBoard);
        }
        else
        {   Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public Document getScoreBoard(int matchId) {
        List<Document> scoreboards = scoreBoardCollection.find(Filters.eq("matchId",matchId)).limit(1).into(new ArrayList<>());
        Document scoreboard=(Document)scoreboards.get(0);
        return scoreboard;
    }
}
