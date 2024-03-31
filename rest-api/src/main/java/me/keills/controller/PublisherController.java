package me.keills.controller;

import me.keills.model.Publisher;
import me.keills.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/publisher")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;
    @PostMapping("/save-json")
    public ResponseEntity<String> savePublisher(@RequestBody Publisher publisher) {
        try{
            publisherService.savePublisher(publisher);
            return ResponseEntity.ok("Publisher saved");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/save")
    public ResponseEntity<String> savePublisher(@RequestParam("name") String publisherName,@RequestParam("url") String publisherUrl) {
        try{
            publisherService.savePublisher(new Publisher(publisherName,publisherUrl));
            return ResponseEntity.ok("Publisher saved");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePublisher(@RequestParam("id") Long id) {
        try{
            publisherService.deletePublisher(id);
            return ResponseEntity.ok("Publisher deleted");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPublisher(@RequestParam("id") Long id) {
        try{
            return ResponseEntity.ok(publisherService.getPublisherById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
