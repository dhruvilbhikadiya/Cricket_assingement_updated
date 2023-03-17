package assignment2.cricket_2.service;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.CommentaryInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentaryService {
    @Autowired
    private final CommentaryInterface commentaryRepository;
    MongoDatabase database = ConnectMongo.getConnection() ;
    @Autowired
    private final PlayerService playerService;
    MongoCollection<Document> commentaryCollection = database.getCollection("commentary") ;
    public ResponseEntity<Document> getBallCommentray(int matchId,int inningsId,int ball)
    {
        Document doc=getMatchCommentary(matchId);
        if(doc!=null)
        {
            Document doc1;
            if(inningsId==1)
                doc1=(Document) doc.get("Innings1");
            else
                doc1=(Document) doc.get("Innings2");
            String s="ball"+ball;
            Document doc2 = (Document) doc1.get(s);
            return ResponseEntity.accepted().body(doc2);
        }
        else
        {   Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public Document addballcommentary(int strike1, int strike2, int bowler, int ballResult,int ballNumber,Document commentary) {
        Document ballcommentary= new Document("_id", new ObjectId()) ;
        ballcommentary.append("bowlNumber",ballNumber)
                .append("bowlerId",bowler)
                .append("bowlerName",playerService.getPlayerName(bowler))
                .append("strike1Id",strike1)
                .append("strike1Name",playerService.getPlayerName(strike1))
                .append("strike2Id",strike2)
                .append("strike2Name",playerService.getPlayerName(strike2));
        if(ballResult==7)
        {
            String s="Wicket";
            ballcommentary.append("Result",s);
        }
        else
        {
            String s=ballResult+"Runs";
            ballcommentary.append("Result",s);
        }
        String s="ball"+ballNumber;
        commentary.append(s,ballcommentary);
        return commentary;
    }
    public Document getMatchCommentary(int matchId) {
        List<Document> commentarys = commentaryCollection.find(Filters.eq("matchId",matchId)).limit(1).into(new ArrayList<>());
        return (Document) commentarys.get(0);
    }
    public void addMatchId(int matchId)
    {
        Document commentary = new Document("_id", new ObjectId()) ;
        commentary.append("matchId",matchId);
        commentaryCollection.insertOne(commentary);
    }
    public void innings1(Document doc,int matchId)
    {
        commentaryCollection.updateOne(Filters.eq("matchId", matchId), Updates.set("Innings1",doc));
    }
    public void innings2(Document doc,int matchId)
    {
        commentaryCollection.updateOne(Filters.eq("matchId", matchId), Updates.set("Innings2",doc));
    }
}
