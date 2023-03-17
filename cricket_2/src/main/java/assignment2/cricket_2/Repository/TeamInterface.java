package assignment2.cricket_2.Repository;

import org.springframework.stereotype.Repository;

@Repository
public interface TeamInterface {
    public void clearData(int teamId);

    public int getPlayer(int playerNum, int teamId);

}
