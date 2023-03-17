package assignment2.cricket_2.service;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.TeamInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    @Autowired
   private final CreateTeam createTeam;
    @Autowired
    private final TeamInterface teamRepository;
   private final MongoDatabase database = ConnectMongo.getConnection() ;
  private final  MongoCollection<Document> teamCollection = database.getCollection("teams") ;
  private final  MongoCollection<Document> playerCollection = database.getCollection("players") ;
    public ResponseEntity<Document> getTeamDetails(int teamId)
    {
        Document team=getTeam(teamId);
        if(team!=null)
        {
            return ResponseEntity.accepted().body(team);
        }
        else
        {
            Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public ResponseEntity<Document> addTeamDetails(String teamDetails)
    {String[] s = teamDetails.split(",") ;
        if(s.length>=12) {
            createTeam.addTeam(teamDetails);
            Document responseDoc=new Document();
            responseDoc.append("Response","Team added");
            return ResponseEntity.accepted().body(responseDoc);
        }
        else
        {
            Document responseDoc=new Document();
            responseDoc.append("ERROR","Please add valid data");
            return ResponseEntity.badRequest().body(responseDoc);
        }
    }
    public String getTeamName(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (String) players.get(0).get("teamName") ;
    }
    public int getTeamBallsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamBallsInMatch") ;
    }
    public int getTeamRunsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamRunsInMatch") ;
    }
    public void increaseTeamBallsInMatch(int teamId)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamBallsInMatch",1)) ;
    }
    public int getPlayer(int playerNum, int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        return playerIds.get(playerNum) ;
    }
    public int getTeamWicketsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamWicketsInMatch") ;
    }
    public void increaseTeamRunsInMatch(int teamId, int runs)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamRunsInMatch",runs)) ;
    }
    public void increaseTeamWicketsInMatch(int teamId)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamWicketsInMatch",1)) ;
    }

    public ArrayList<Integer> getPlayerList(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (ArrayList<Integer>)players.get(0).get("players");
    }

    public Document getTeam(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        Document team=(Document)players.get(0) ;
        return team;
    }
}
