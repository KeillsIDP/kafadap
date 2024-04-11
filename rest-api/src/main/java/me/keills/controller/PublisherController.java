package me.keills.controller;

import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Publisher;
import me.keills.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для обработки операций, связанных с издателями.
 */
@RestController
@RequestMapping("api/publisher")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    /**
     * Сохранить издателя из JSON объекта.
     * @param publisher {@link Publisher} - объект издателя для сохранения
     * @return {@link ResponseEntity} с сообщением об успешном сохранении или ошибкой
     */
    @PostMapping("/save-json")
    public ResponseEntity<String> savePublisher(@RequestBody Publisher publisher) {
        try{
            publisherService.savePublisher(publisher);
            return ResponseEntity.ok("Publisher saved");
        } catch(PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Сохранить издателя из параметров запроса.
     * @param publisherName {@link String} - имя издателя
     * @param publisherUrl {@link String} - URL издателя
     * @return {@link ResponseEntity} с сообщением об успешном сохранении или ошибкой
     */
    @PostMapping("/save")
    public ResponseEntity<String> savePublisher(@RequestParam("name") String publisherName,@RequestParam("url") String publisherUrl) {
        try{
            publisherService.savePublisher(new Publisher(publisherName,publisherUrl));
            return ResponseEntity.ok("Publisher saved");
        } catch(PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удалить издателя по ID.
     * @param id {@link Long} - ID издателя для удаления
     * @return {@link ResponseEntity} с сообщением об успешном удалении или ошибкой
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePublisher(@RequestParam("id") Long id) {
        try{
            publisherService.deletePublisher(id);
            return ResponseEntity.ok("Publisher deleted");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получить издателя по ID.
     * @param id {@link Long} - ID издателя для получения
     * @return {@link ResponseEntity} с объектом издателя или ошибкой
     */
    @GetMapping("/get")
    public ResponseEntity<?> getPublisher(@RequestParam("id") Long id) {
        try{
            return ResponseEntity.ok(publisherService.getPublisherById(id));
        } catch (PublisherNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
