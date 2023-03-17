package assignment2.cricket_2;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Team {
    private int teamId;
    private int totalBallsPlayed;
    private int totalRunsScored;
    private int totalWicketTaken;
    private int toatalThrownBalls;
    private int totalWicket;
    private List<Batsman> batsmen=new ArrayList<Batsman>();
    private List<Bowler> bowler=new ArrayList<Bowler>();
    public void addBatsmen(int playerId,int teamId)
    {
        batsmen.add(new Batsman(playerId,teamId));
    }
    public void addBowler(int playerId,int teamId)
    {
        bowler.add(new Bowler(playerId,teamId));
    }
}
