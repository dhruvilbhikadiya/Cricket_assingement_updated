package assignment2.cricket_2;

import lombok.Data;

@Data
public class Bowler {
    private int playerId;
    private int matchId;
    private int ballsThrown;
    private int wicketsTaken;
    private int runsGiven;
    private int score;
    private int ballsPlayed;
    public Bowler(int playerId,int matchId)
    {
        this.playerId=playerId;
        this.matchId=matchId;
    }
}
