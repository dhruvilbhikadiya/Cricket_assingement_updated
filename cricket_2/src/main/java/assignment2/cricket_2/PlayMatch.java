package assignment2.cricket_2;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.Repository.ScoreBoardInterface;
import assignment2.cricket_2.Repository.TeamInterface;
import assignment2.cricket_2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.gte;
@Service
public class PlayMatch {
    static int matchId = 0;
    private int oversOfMatch;
    @Autowired
    TeamInterface teamRepository ;

    @Autowired
    ScoreBoardInterface scoreBoardRepository;

    @Autowired
    TeamService teamService;
    @Autowired
    BatsmanService batsmanService;
    @Autowired
    BowlerService bowlerService;
    @Autowired
    PlayerService playerService;
    @Autowired
    CommentaryService commentaryService;
    private int team1Id, team2Id;
    Scanner sc = new Scanner(System.in);
    assignment2.cricket_2.service.RandomNumberGenerator rg = new RandomNumberGenerator();
    String tossResult = "";
    String tossResultOutput="";
    String matchResult="";
    private Team team1;
    private Team team2;


    public void playMatch(int team1Id,int team2Id)
    {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        oversOfMatch = 60;
        MongoDatabase database= ConnectMongo.getConnection();
        MongoCollection<Document> scoreBoardCollection=database.getCollection("scoreboards");
        List<Document> matchSize=scoreBoardCollection.find(gte("matchId",0)).into(new ArrayList<>());

        if(matchSize.size()==0)
            matchId=0;
        else
            matchId=(int)matchSize.get(0).get("matchId")+1;
        createMatchdata();
        toss();
        startMatch();
        playerService.saveData(matchId,team1);
        playerService.saveData(matchId,team2);
        printScoreBoard();

        teamRepository.clearData(team1Id) ;
        teamRepository.clearData(team2Id) ;
    }
    public void createMatchdata()
    {

        team1=createMatchPlayers(team1Id);
        team2=createMatchPlayers(team2Id);
    }
    public Team createMatchPlayers(int teamId)
    {   Team team=new Team();
        List<Integer> teamPlayer= (List<Integer>) teamService.getPlayerList(teamId);
        for(int i=0;i<11;i++)
        {
            System.out.println(teamPlayer.get(i));
            team.addBatsmen(teamPlayer.get(i),teamId);
        }
        for(int i=7;i<11;i++)
        {    System.out.println(teamPlayer.get(i));
            team.addBowler(teamPlayer.get(i),teamId);
        }
        team.setTeamId(teamId);
        return team;
    }
    public void toss() {

        char guessToss = sc.next().charAt(0);
        char generateToss = (rg.generateRandomNumber(1) == 0) ? 'H' : 'T';
        tossResult = (guessToss == generateToss) ? "0" : "1";
        int answer = sc.nextInt();
        tossResult += answer;
    }
    public void startMatch() {
        System.out.print("\n");
        String[] s1 = new String[2];
        s1[0] = Integer.toString(team1.getTeamId());//
        s1[1] = Integer.toString(team2.getTeamId());//
        String[] s2 = new String[2];//
        s2[0] = "Bat";//
        s2[1] = "Bowl";//
        tossResultOutput = s1[tossResult.charAt(0) - '0'] + " won the toss and choose to " +
                s2[tossResult.charAt(1) - '0'] + " first."; //
        System.out.println(tossResultOutput + "\n");//
        List<BallCommentary> inning1;
        List<BallCommentary> inning2;
        if (tossResult.equals("01") || tossResult.equals("10")) {
            int t = team1Id;
            team1Id = team2Id;
            team2Id = t;
         inning1 = startMatch(team2, team1, 1, -1);
            inning2 = startMatch(team1, team2, 2, team2.getTotalRunsScored());

        } else {
           inning1 = startMatch(team1, team2, 1, -1);
           inning2 = startMatch(team2, team1, 2, team1.getTotalRunsScored());
        }
       commentaryService.addMatchId(matchId);
        Document inning1Doc=creatInningsCommentory(inning1);
        commentaryService.innings1(inning1Doc,matchId);
        Document inning2Doc=creatInningsCommentory(inning2);
        commentaryService.innings2(inning2Doc,matchId);
    }
    public Document creatInningsCommentory(List<BallCommentary> innings)
    {   Document commentary=new Document();
        for(int i=0;i<innings.size();i++)
        {
            Document ballcommentary= new Document() ;
            ballcommentary.append("bowlNumber",innings.get(i).getBallNum())
                    .append("bowlerId",innings.get(i).getBowler())
                    .append("strike1Id",innings.get(i).getStriker1())
                    .append("strike2Id",innings.get(i).getStriker2());
            if(innings.get(i).getResult()==7)
            {
                String s="Wicket";
                ballcommentary.append("Result",s);
            }
            else
            {
                String s=innings.get(i).getResult()+"Runs";
                ballcommentary.append("Result",s);
            }
            String s="ball"+innings.get(i).getBallNum();
            commentary.append(s,ballcommentary);

        }
        return commentary;
    }

    public List<BallCommentary> startMatch(Team team1Id,Team team2Id,int flag,int target)
    {   List<BallCommentary> inning=new ArrayList<>();
      //  System.out.println(teamService.getTeamName(team1Id) + " Batting : \n");//
        int strike1 = 0;
        int strike2 = 1;
        int bowler = 7;
        int nextPlayer = 2;
        int nextBowler = 8;
        int balls=0;
        int runs=0;
        while(balls<oversOfMatch)
        {
            if(flag==2&&team1Id.getTotalRunsScored()>team2Id.getTotalRunsScored())
            {
                break;
            }
            balls++;
            int ballResult = rg.generateRandomNumber(7);
            increaseBalls(strike1,bowler,team1Id,team2Id);
            inning.add(addBallCommentary(strike1,strike2,bowler,ballResult,balls-1));
            if(ballResult==1)
            {
                wicketTaken(strike1,bowler,team1Id,team2Id,matchId);
                if(team1Id.getTotalWicket()==10)
                {
                    break;
                }
                strike1=nextPlayer++;
            }
            else
            {
               team1Id.setTotalRunsScored(team1Id.getTotalRunsScored()+ballResult);
               team1Id.getBatsmen().get(strike1).setScore(team1Id.getBatsmen().get(strike1).getScore()+ballResult);
               if(ballResult==4)
               {
                   team1Id.getBatsmen().get(strike1).setMatch4s(team1Id.getBatsmen().get(strike1).getMatch4s()+ballResult);
               }
               else if(ballResult==6)
               {
                   team1Id.getBatsmen().get(strike1).setMatch6s(team1Id.getBatsmen().get(strike1).getMatch6s()+ballResult);
               }
               team2Id.getBowler().get(bowler-7).setRunsGiven(team2Id.getBowler().get(bowler-7).getRunsGiven()+ballResult);
                if (ballResult % 2 == 1) {
                    int temp = strike1;
                    strike1 = strike2;
                    strike2 = temp;
                }
            }
            if (team1Id.getTotalBallsPlayed() % 6 == 0) {
                int temp = strike1;
                strike1 = strike2;
                strike2 = temp;
                bowler = nextBowler++;
                if (nextBowler == 11) {
                    nextBowler = 7;
                }
            }
        }
        return inning;
    }

    public BallCommentary addBallCommentary(int striker1,int striker2,int bowler,int ballResult,int balls)
    {
        BallCommentary ballCommentary=new BallCommentary();
        ballCommentary.setBallNum(balls);
        ballCommentary.setResult(ballResult);
        ballCommentary.setStriker1(striker1);
        ballCommentary.setStriker2(striker2);
        ballCommentary.setBowler(bowler);
        return ballCommentary;
    }
    public void wicketTaken(int strike1, int bowler,Team team1,Team team2,int matchId)
    {
        team1.setTotalWicket(team1.getTotalWicket()+1);
        team2.getBowler().get(bowler-7).setWicketsTaken(team2.getBowler().get(bowler-7).getWicketsTaken()+1);
    }
    public void increaseBalls(int strike1,int bowler,Team team1,Team team2)
    {
        team1.getBatsmen().get(strike1).setBallsPlayed(team1.getBatsmen().get(strike1).getBallsPlayed()+1);
        team2.getBowler().get(bowler-7).setBallsThrown(team2.getBowler().get(bowler-7).getBallsThrown()+1);
    }

    void winner() {
        if (team2.getTotalRunsScored() < team1.getTotalRunsScored() ) {
            matchResult=team1.getTeamId()  + " won the match by " ;
            System.out.println(matchResult);
        } else if (team1.getTotalRunsScored() < team2.getTotalRunsScored() ) {
            matchResult=team2.getTeamId() + " won the match by " ;
            System.out.println(matchResult);
        } else {
            matchResult="Match Tied.";
            System.out.println(matchResult);
        }
    }
    void printScoreBoard() {
        System.out.println();
        winner();
        scoreBoardRepository.saveScoreBoard(team1Id,team2Id,tossResultOutput,matchId,matchResult);
    }
}