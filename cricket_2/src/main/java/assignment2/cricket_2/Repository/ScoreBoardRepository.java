package assignment2.cricket_2.Repository;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.service.BatsmanService;
import assignment2.cricket_2.service.BowlerService;
import assignment2.cricket_2.service.PlayerService;
import assignment2.cricket_2.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;
@Component
public class ScoreBoardRepository implements ScoreBoardInterface{
    MongoDatabase database = ConnectMongo.getConnection() ;
    @Autowired
    TeamService teamService;
    @Autowired
    PlayerService playerService;
    @Autowired
    BatsmanService batsmanService;
    @Autowired
    BowlerService bowlerService;

    MongoCollection<Document> scoreBoardCollection = database.getCollection("scoreboards");
    public void saveScoreBoard(int team1Id,int team2Id,String tossResultOutput,int matchId,String matchResult)
    {    Document scoreBoard = new Document("_id", new ObjectId()) ;
        scoreBoard.append("matchId",matchId)
                .append("team1Name",teamService.getTeamName(team1Id))
                .append("team2Name",teamService.getTeamName(team2Id))
                .append("tossResult",tossResultOutput);
        Document inning1=saveInnings(team1Id,team2Id,matchId);
        Document inning2=saveInnings(team2Id,team1Id,matchId);
        scoreBoard.append("innings1",inning1);
        scoreBoard.append("innings2",inning2);
        scoreBoard.append("MatchResult",matchResult);
        scoreBoardCollection.insertOne(scoreBoard);
    }
   public Document saveInnings(int team1Id,int team2Id,int matchId)
    {
        Document inning = new Document("_id", new ObjectId()) ;
        Document batting = new Document("_id", new ObjectId()) ;
        for(int i=0;i<11;i++)
        {   String s="Player"+i;
            Document batman=saveBatting(teamService.getPlayer(i,team1Id),matchId);
            batting.append(s,batman);
        }
        inning.append("Batting",batting);
        Document bowling = new Document("_id", new ObjectId()) ;
        for(int i=7;i<11;i++)
        {
            String s="Player"+i;
            Document bowler=saveBowling(teamService.getPlayer(i,team1Id),matchId);
            bowling.append(s,bowler);
        }
        inning.append("Bowling",bowling);
        Document inningResult = new Document("_id", new ObjectId()) ;
        inningResult.append("Score",teamService.getTeamRunsInMatch(team1Id))
                .append("Over", teamService.getTeamBallsInMatch(team1Id));
        inning.append("Result",inningResult);
        return inning;
    }
    public Document saveBowling(int playerId,int matchId)
    {
        Document player=new Document("_id",new ObjectId());
        player.append("playerName",playerService.getPlayerName(playerId))
                .append("playerThrownBalls",bowlerService.getBowlerThrownBalls(playerId,matchId))
                .append("playerGivenRuns",bowlerService.getBowlerGivenRuns(playerId,matchId))
                .append("playerTakenWickets",bowlerService.getBowlerTakenWickets(playerId,matchId));
        return player;
    }
   public Document saveBatting(int playerId,int matchId)
    {
        Document player=new Document("_id",new ObjectId());
        player.append("playerName",playerService.getPlayerName(playerId))
                .append("playerRubs",batsmanService.getPlayerScore(playerId,matchId))
                .append("playerBalls",batsmanService.getPlayerPlayedBalls(playerId,matchId))
                .append("player4s",batsmanService.getPlayer4s(playerId,matchId))
                .append("player6s",batsmanService.getPlayer6s(playerId,matchId));
        return player;
    }
}
