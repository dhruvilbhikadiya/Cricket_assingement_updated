package assignment2.cricket_2.Controller;

import assignment2.cricket_2.service.ScoreBoardService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequestMapping(path="/scoreboard",
        produces="application/json")
public class ScoreBoardController {
    //ScoreBoardRepository scoreBoardRepository=new ScoreBoardRepository();
    @Autowired
    private ScoreBoardService scoreBoardService;
    @GetMapping("/get/match/{matchId}")
     public ResponseEntity<Document> getScoreBoardOfMatch(@PathVariable("matchId") int matchId)
    {
        return scoreBoardService.getScoreBoardOfMatch(matchId);
    }
}
