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

/**
 * Контроллер для обработки HTTP-запросов, связанных с играми.
 */
@RestController
@RequestMapping("api/game")
public class GameController {
    @Autowired
    private GameService gameService;

    /**
     * Метод для сохранения игры.
     * @param game {@linkplain Game} - объект игры для сохранения
     * @return {@linkplain ResponseEntity} с сообщением об успешном сохранении или ошибкой.
     */
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

    /**
     * Метод для сохранения игры с указанными параметрами.
     * @param gameName {@linkplain String} - название игры
     * @param releaseDate {@linkplain String} - дата выпуска игры
     * @param price {@linkplain Integer} - цена игры
     * @param publisherId {@linkplain Long} - идентификатор издателя (необязательный)
     * @return {@linkplain ResponseEntity} с сообщением об успешном сохранении или ошибкой.
     */
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

    /**
     * Метод для изменения цены игры.
     * @param id {@linkplain Long} - идентификатор игры
     * @param price {@linkplain Integer} - новая цена игры
     * @return {@linkplain ResponseEntity} с сообщением об успешном изменении цены или ошибкой.
     */
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

    /**
     * Метод для удаления игры.
     * @param id {@linkplain Long} - идентификатор игры
     * @return {@linkplain ResponseEntity} с сообщением об успешном удалении игры или ошибкой.
     */
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

    /**
     * Метод для получения игры по идентификатору.
     * @param id {@linkplain Long} - идентификатор игры
     * @return {@linkplain ResponseEntity} с объектом игры или ошибкой, если игра не найдена.
     */
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

    /**
     * Метод для получения игры по идентификатору издателя.
     * @param id {@linkplain Long} - идентификатор издателя
     * @return {@linkplain ResponseEntity} с листом игр или ошибкой, если издатель не найдена.
     */
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

    /**
     * Метод для получения списка игр по фильтру.
     * @param filter {@linkplain GameFilter } - фильтр для поиска игр
     * @param value {@linkplain String} - значение фильтра
     * @return {@linkplain ResponseEntity} со списком игр, удовлетворяющих фильтру, или ошибкой.
     */
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
