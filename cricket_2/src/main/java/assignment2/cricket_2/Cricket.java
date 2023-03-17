package assignment2.cricket_2;
import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.TeamInterface;
import assignment2.cricket_2.service.CreateTeam;
import assignment2.cricket_2.service.RandomNumberGenerator;
import assignment2.cricket_2.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.LoggerFactory;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.gte;
@Service
public class Cricket {
    @Autowired
    private TeamInterface teamRepository;
    @Autowired
    private PlayMatch playingMatch;
    @Autowired
    private TeamService teamService;

    public void begin() throws FileNotFoundException {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        ((Logger)rootLogger).setLevel(Level.OFF);

        Scanner sc = new Scanner(System.in);

        MongoDatabase database = ConnectMongo.getConnection() ;

        MongoCollection<Document> collection1 = database.getCollection("teams") ;
        List<Document> team = collection1.find(gte("teamId",0)).limit(1).into(new ArrayList<>());
        int teamsSize = (team.size()==0) ? 0 : (int)team.get(0).get("teamId")+1 ;
        if(teamsSize<4)
        {
            String URL = "//Users//bdmanjibhai//Downloads//Ass.txt" ;
            CreateTeam teams = new CreateTeam();
            teams.createTeam(URL);
            team = collection1.find(gte("teamId",0)).limit(1).into(new ArrayList<>());
            teamsSize = (team.size()==0) ? 0 : (int)team.get(0).get("teamId")+1 ;
        }
        teamsSize-- ;
        RandomNumberGenerator rg = new RandomNumberGenerator();

        //starting match
        char playMoreMatch;
        do {
            int team1_id = rg.generateRandomNumber(teamsSize);
            int team2_id = rg.generateRandomNumber(teamsSize);
            while (team1_id == team2_id) {
                team2_id = rg.generateRandomNumber(teamsSize);
            }
            if(team1_id>team2_id)
            {
                int temp=team2_id;
                team2_id=team1_id;
                team1_id=temp;
            }
            teamRepository.clearData(team1_id) ;
            teamRepository.clearData(team2_id) ;
            System.out.println(team1_id + " " + team2_id);

            System.out.println("\nLet's Begin  " + teamService.getTeamName(team1_id) + " v/s " + teamService.getTeamName(team2_id));
            playingMatch.playMatch(team1_id, team2_id);
            System.out.print("\nPlay More Match (y/n) : ");
            playMoreMatch = sc.next().charAt(0);

        } while (playMoreMatch == 'y');

    }
}