package assignment2.cricket_2.Controller;

import assignment2.cricket_2.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@ResponseBody
@RequestMapping(path="/player",
        produces="application/json")

public class PlayerController {
//    BatsmanRepository batsmanRepository=new BatsmanRepository();
//    BowlerRepository bowlerRepository=new BowlerRepository();
//    PlayerRepository playerRepository=new PlayerRepository();
@Autowired
    private PlayerService playerService;
    @GetMapping("/get/{playerId}")
    public ResponseEntity< Document> getPlayerData(@PathVariable("playerId") int playerId)
    {
        return playerService.getPlayerData(playerId);
    }
    @GetMapping("/get/statistics/{playerId}")
    public ResponseEntity<Document> getPlayerStatistics(@PathVariable("playerId") int playerId)
    {
        return playerService.getPlayerStatistics(playerId);
    }
    @GetMapping("/get/match/{matchId}/player/{playerId}")
    public ResponseEntity<Document> getPlayerDataInMatch(@PathVariable("matchId") int matchId, @PathVariable("playerId") int playerId)
    {
        return playerService.getPlayerDataInMatch(matchId,playerId);
    }
    @GetMapping("/get/match/{matchId}/batsman/{batsmanId}")
    public ResponseEntity<Document> getBatsmanDataInMatch(@PathVariable("matchId") int matchId, @PathVariable("batsmanId")int batsmanId)
    {
       return playerService.getBatsmanDataInMatch(matchId,batsmanId);
    }
    @GetMapping("/get/match/{matchId}/bowler/{bowlerId}")
    public ResponseEntity<Document> getBowlerDataInMatch(@PathVariable("matchId") int matchId,@PathVariable("bowlerId")int bowlerId)
    {
       return playerService.getBowlerDataInMatch(matchId,bowlerId);
    }
}
