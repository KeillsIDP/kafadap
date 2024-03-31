package me.keills.controller;

import me.keills.exception.game.GameFilterIsIncorrectException;
import me.keills.exception.game.GameFilterValueIsIncorrectException;
import me.keills.exception.game.GameNotFoundException;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Game;
import me.keills.service.GameService;
import me.keills.util.GameFilter;
import org.apache.kafka.common.requests.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/save-json")
    public ResponseEntity<String> saveGame(@RequestBody Game game){
        try{
            gameService.saveGame(game);
            return ResponseEntity.ok("Game saved");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveGame(@RequestParam("gameName") String gameName,
                                           @RequestParam("releaseDate") String releaseDate,
                                           @RequestParam("price") int price,
                                           @RequestParam(value = "publisherId",required = false) Long publisherId) {
        try{
            if(publisherId!=null)
                gameService.saveGame(new Game(gameName, LocalDate.parse(releaseDate),price),publisherId);
            else
                gameService.saveGame(new Game(gameName, LocalDate.parse(releaseDate),price));
            return ResponseEntity.ok("Game saved");
        } catch (PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editGamePrice(@RequestParam("id") Long id,@RequestParam("price") int price){
        try{
            gameService.editGamePrice(id,price);
            return ResponseEntity.ok("Game edited");
        } catch (GameNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit-publisher")
    public ResponseEntity<String> editGamePublisher(@RequestParam("id") Long id,@RequestParam(name = "publisherId",required = false) Long publisherId){
        try{
            if(publisherId!=null)
                gameService.editGamePublisher(id,publisherId);
            else
                gameService.editGamePublisher(id,null);
            return ResponseEntity.ok("Game edited");
        } catch (PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GameNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGame(@RequestParam("id") Long id){
        try{
            gameService.deleteGame(id);
            return ResponseEntity.ok("Game deleted");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getGame(@RequestParam("id") Long id){
        try{
            return ResponseEntity.ok(gameService.getGame(id));
        } catch (GameNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-by-publisher")
    public ResponseEntity<?> getGamesByPublisher(@RequestParam("id") Long id){
        try{
            return ResponseEntity.ok(gameService.getGamesByPublisher(id));
        } catch (PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-by-filter")
    public ResponseEntity<?> getGamesByFilter(@RequestParam("filter") GameFilter filter, @RequestParam("value") String value){
        try{
            return ResponseEntity.ok(gameService.getGamesByFilter(filter,value));
        } catch (GameFilterIsIncorrectException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GameFilterValueIsIncorrectException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
