package assignment2.cricket_2.Repository;

import assignment2.cricket_2.Connection.ConnectMongo;
import assignment2.cricket_2.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;

@Component
public class CommentaryRepository implements CommentaryInterface{
    MongoDatabase database = ConnectMongo.getConnection() ;
@Autowired
  TeamInterface teamRepository;
   @Autowired
  PlayerInterface playerRepository;
    @Autowired
    PlayerService playerService;
   MongoCollection<Document> commentaryCollection = database.getCollection("commentary") ;


}
