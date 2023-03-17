package assignment2.cricket_2.service;

import assignment2.cricket_2.Batsman;
import assignment2.cricket_2.Bowler;
import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.BatsmanInterface;
import assignment2.cricket_2.Repository.BowlerInterface;
import assignment2.cricket_2.Repository.PlayerInterface;
import assignment2.cricket_2.Team;
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
public class PlayerService {
    @Autowired
    private final PlayerInterface playerRepository;
    @Autowired
    private final BatsmanInterface batsmanRepository;
    @Autowired
    private final BowlerInterface bowlerRepository;
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> playerCollection = database.getCollection("players") ;
    MongoCollection<Document> teamCollection = database.getCollection("teams") ;
    MongoCollection<Document> batsmanCollection = database.getCollection("batsman") ;
    MongoCollection<Document> bowlerCollection = database.getCollection("bowler") ;
    public ResponseEntity<Document> getPlayerData(int playerId)
    {
        Document player=playerRepository.getPlayer(playerId);
        if(player != null)
        {

            return ResponseEntity.accepted().body(player);
        }
        else
        {   Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public ResponseEntity<Document> getPlayerStatistics(int playerId) {
        Document player =getPlayer(playerId);
        if (player != null) {
            Document stats = new Document();
            int runs_scored = (int) player.get("playerTotalScore");
            int balls_faced = (int) player.get("playerTotalPlayedBalls");
            double strikeRate = (runs_scored / (balls_faced * 1.0)) * 100;
            int total_out = (int) player.get("playerTotalOut");
            if (total_out == 0)
                total_out = 1;
            double average = (runs_scored / (total_out * 1.0));
            stats.append("playerId", player.get("playerId"))
                    .append("playerName", player.get("playerName"))
                    .append("playerTotalPlayedBalls", player.get("playerTotalPlayedBalls"))
                    .append("playerTotalScore", player.get("playerTotalScore"))
                    .append("playerStrikeRate", strikeRate)
                    .append("playerAverage", average);
            return ResponseEntity.accepted().body(player);
        }
        else
        {
            Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public ResponseEntity<Document> getPlayerDataInMatch(int matchId,int playerId) {
        Document batsman = batsmanRepository.getPlayer(playerId, matchId);
        Document player = new Document();
        Document bowler = null;
        if (batsman != null) {
            player.append("playername", getPlayerName(playerId))
                    .append("playerScoreInmatch", batsman.get("batsmanScore"))
                    .append("playerPlayedBalls", batsman.get("batsmanPlayedBalls"))
                    .append("player4sInmatch", batsman.get("batsman4s"))
                    .append("player6sInmatch", batsman.get("batsman6s"));
            bowler = bowlerRepository.getPlayer(playerId, matchId);
        }
        if (bowler != null) {
            player.append("playerTakenWickets", bowler.get("bowlerTakenWickets"))
                    .append("playerThrownBalls", bowler.get("bowlerThrownBalls"))
                    .append("playerGivenRuns", bowler.get("bowlerGivenRuns"));
        }
        if(player!=null)
        {
            return ResponseEntity.accepted().body(player);
        }
        else
        {
            Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }

    }
    public ResponseEntity<Document> getBatsmanDataInMatch( int matchId,int batsmanId)
    {
        Document batsman=batsmanRepository.getPlayer(batsmanId,matchId);
        if(batsman!= null)
        {

            return ResponseEntity.accepted().body(batsman);
        }
        else
        {   Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public ResponseEntity<Document> getBowlerDataInMatch(int matchId,int bowlerId)
    {
        Document bowler=bowlerRepository.getPlayer(bowlerId,matchId);
        if(bowler!= null)
        {

            return ResponseEntity.accepted().body(bowler);
        }
        else
        {   Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public void increaseBowlerThrownBalls(int playerId)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalThrownBalls",1)) ;
    }

    public void increasePlayerPlayedBalls(int playerId,int balls)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalPlayedBalls",balls)) ;
    }
    public String getPlayerName(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (String) players.get(0).get("playerName") ;
    }
    public void increasePlayerTotalOut(int playerId)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalOut",1)) ;
    }

    public void increaseBowlerTakenWickets(int playerId,int wickets)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalTakenWickets",wickets)) ;
    }
    public Document getPlayer(int playerId) {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        Document player=(Document) players.get(0) ;
        return player;
    }
    public void increaseBowlerGivenRuns(int playerId,int runs)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalGivenRuns",runs)) ;
    }

    public void increasePlayerScore(int playerId, int runs)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalScore",runs)) ;
        if(runs==4)
        {

            playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotal4s",1)) ;
        }
        else if(runs==6)
        {
            playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotal6s",1)) ;
        }
    }
    public void saveData(int matchId, Team team)
    {
        List<Batsman> batsmen=team.getBatsmen();
        List<Bowler> bowlers=team.getBowler();

           createPlayers(matchId,team);

        savePlayerData(team);
    }
    public void savePlayerData(Team team)
    { List<Batsman> batsmen=team.getBatsmen();
        List<Bowler> bowlers=team.getBowler();
        for(int i=0;i<11;i++)
        {
            increasePlayerScore(batsmen.get(i).getPlayerId(),batsmen.get(i).getScore());
            increasePlayerPlayedBalls(batsmen.get(i).getPlayerId(),batsmen.get(i).getBallsPlayed());
            if(i>=7)
            {
                increaseBowlerTakenWickets(bowlers.get(i-7).getPlayerId(),bowlers.get(i-7).getWicketsTaken());
                increaseBowlerGivenRuns(bowlers.get(i-7).getPlayerId(),bowlers.get(i-7).getRunsGiven());
            }
        }
    }
    public void createPlayers(int matchId,Team team)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",team.getTeamId())).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        for(int i=0;i<11;i++)
        {
            Document batsman = new Document("_id", new ObjectId()) ;
            String playerName=getPlayerName(playerIds.get(i));
            batsman.append("batsmanId",playerIds.get(i))
                    .append("batsmanName",playerName)
                    .append("batsmanMatchId",matchId)
                    .append("batsmanScore",team.getBatsmen().get(i).getScore())
                    .append("batsmanPlayedBalls",team.getBatsmen().get(i).getBallsPlayed())
                    .append("batsman4s",0)
                    .append("batsman6s",0);
            if(i>=7)
            {Document bowler = new Document("_id", new ObjectId()) ;
                bowler.append("bowlerId",playerIds.get(i))
                        .append("bowlerName",playerName)
                        .append("bowlerMatchId",matchId)
                        .append("bowlerTakenWickets",team.getBowler().get(i-7).getWicketsTaken())
                        .append("bowlerThrownBalls",team.getBowler().get(i-7).getBallsThrown())
                        .append("bowlerGivenRuns",team.getBowler().get(i-7).getRunsGiven());
                bowlerCollection.insertOne(bowler);
            }
            batsmanCollection.insertOne(batsman);
        }
    }

}
