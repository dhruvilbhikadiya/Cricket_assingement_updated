package assignment2.cricket_2.Repository;

import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreBoardInterface {
    public void saveScoreBoard(int team1Id,int team2Id,String tossResultOutput,int matchId,String matchResult);
    public Document saveInnings(int team1Id, int team2Id, int matchId);
    public Document saveBowling(int playerId,int matchId);
    public Document saveBatting(int playerId,int matchId);

}
