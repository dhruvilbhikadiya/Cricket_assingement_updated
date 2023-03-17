package assignment2.cricket_2;

import lombok.Data;

@Data
public class Batsman {
    private int playerId;
    private int matchId;
    private int score;
    private int ballsPlayed;
    private int match4s;
    private int match6s;
    Batsman()
    {}
    public Batsman(int playerId,int matchId)
    {
        this.playerId=playerId;
        this.matchId=matchId;
    }
}
