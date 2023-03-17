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
public class TeamRepository implements TeamInterface
{
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> teamCollection = database.getCollection("teams") ;
    MongoCollection<Document> playerCollection = database.getCollection("players") ;

    public void clearData(int teamId)
    {

        teamCollection.updateOne(Filters.eq("teamId",teamId), Updates.combine(
                Updates.set("teamRunsInMatch",0),
                Updates.set("teamWicketsInMatch",0),
                Updates.set("teamBallsInMatch",0)
        )) ;

        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        int cnt = 0 ;
        for(Integer i:playerIds)
        {
            playerCollection.updateOne(Filters.eq("playerId",i),
                    Updates.combine(
                            Updates.set("playerScore",0),
                            Updates.set("player4s",0),
                            Updates.set("player6s",0),
                            Updates.set("playerPlayedBalls",0)
                    )) ;
            if(cnt>7)
            {
                playerCollection.updateOne(Filters.eq("playerId",i),
                        Updates.combine(
                                Updates.set("bowlerTakenWickets",0),
                                Updates.set("bowlerThrownBalls",0),
                                Updates.set("bowlerGivenRuns",0)
                        )) ;
            }
            cnt++ ;

        }
    }
    public int getPlayer(int playerNum, int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        return playerIds.get(playerNum) ;
    }
}
