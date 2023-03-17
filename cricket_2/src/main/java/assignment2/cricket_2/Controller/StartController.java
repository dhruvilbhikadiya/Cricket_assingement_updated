package assignment2.cricket_2.Controller;

import assignment2.cricket_2.Cricket;
import assignment2.cricket_2.PlayMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.FileNotFoundException;

@RestController
@ResponseBody
@RequestMapping(path="/startMatch",
        produces="application/json")
@CrossOrigin(origins="http://localhost:8080")

public class StartController {
    @Autowired
    Cricket cricket;
    @Autowired
    PlayMatch playMatch;
//    ValidationService validationService=new ValidationService();
  @GetMapping("/")
    public void startMatch  ()
    {

        try {
            cricket.begin();
            System.out.println("returned");
           // createTeam.createTeam("//Users//bdmanjibhai//Downloads//Ass.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/team1/{Team1Id}/team2/{Team2Id}")
    public void startMatchWithId (@PathVariable("Team1Id") int Team1Id,@PathVariable("Team2Id") int Team2Id)
    {
            playMatch.playMatch(Team1Id,Team2Id);
            // createTeam.createTeam("//Users//bdmanjibhai//Downloads//Ass.txt");
    }

}
