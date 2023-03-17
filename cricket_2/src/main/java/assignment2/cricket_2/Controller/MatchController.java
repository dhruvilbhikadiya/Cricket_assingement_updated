package assignment2.cricket_2.Controller;

import assignment2.cricket_2.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@ResponseBody
@RequestMapping(path="/team",
        produces="application/json")

public class MatchController {
//    CreateTeam createTeam=new CreateTeam();
//    TeamRepository teamRepository;
@Autowired
    private TeamService matchService;
    @GetMapping("/get/{teamId}")
    public ResponseEntity<Document> getTeamDetails(@PathVariable("teamId") int teamId)
    {
        return matchService.getTeamDetails(teamId);
    }
    @PostMapping("/addteam/{teamDetails}")
    public void addTeamDetails(@PathVariable("teamDetails") String teamDetails)
    {
        matchService.addTeamDetails(teamDetails);
    }
}
