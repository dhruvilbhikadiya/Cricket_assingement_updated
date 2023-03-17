package assignment2.cricket_2.Controller;

import assignment2.cricket_2.service.CommentaryService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@ResponseBody
@RequestMapping(path="/commentary",
        produces="application/json")

public class CommentaryController {

    //private final CommentaryRepository commentaryRepository;
    @Autowired
    private CommentaryService commentaryService;
    @GetMapping("/match/{matchId}/innings/{inningsId}/ball/{ballnumber}")
    public ResponseEntity<Document> getBallCommentary(@PathVariable("matchId")int matchId, @PathVariable("inningsId") int inningsId, @PathVariable("ballnumber") int ball) {

       return commentaryService.getBallCommentray(matchId,inningsId,ball);
    }
}
