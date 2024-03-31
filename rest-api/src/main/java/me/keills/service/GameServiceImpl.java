package me.keills.service;

import me.keills.exception.game.GameFilterIsIncorrectException;
import me.keills.exception.game.GameFilterValueIsIncorrectException;
import me.keills.exception.game.GameNotFoundException;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Game;
import me.keills.model.Publisher;
import me.keills.repo.GameRepo;
import me.keills.repo.PublisherRepo;
import me.keills.util.GameFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GameServiceImpl implements GameService{
    @Autowired
    private GameRepo gameRepo;
    @Autowired
    private PublisherRepo publisherRepo;

    @Override
    public void saveGame(Game game) {
        gameRepo.save(game);
    }

    @Override
    public void saveGame(Game game, Long publisherId) {
        game.setPublisher(publisherRepo.findById(publisherId).orElseThrow(() -> new PublisherNotFoundException("Publisher withd id = " + publisherId + " not found")));
        gameRepo.save(game);
    }

    @Override
    public void editGamePrice(Long id, int price) {
        gameRepo.findById(id).ifPresentOrElse(game -> {
            game.setPrice(price);
            gameRepo.save(game);
            }, () -> {throw new GameNotFoundException("Game with id = " + id + " not found");});
    }

    @Override
    public void editGamePublisher(Long id, Long publisherId) {
        gameRepo.findById(id).ifPresentOrElse(game -> {
            if(publisherId!=null)
                game.setPublisher(publisherRepo.findById(publisherId).orElseThrow(() -> new PublisherNotFoundException("Publisher withd id = " + publisherId + " not found")));
            else
                game.setPublisher(null);
            gameRepo.save(game);
        }, () -> {throw new GameNotFoundException("Game with id = " + id + " not found");});
    }

    @Override
    public void deleteGame(Long id) {
        gameRepo.deleteById(id);
    }

    @Override
    public Game getGame(Long id) {
        return gameRepo.findById(id).orElseThrow(() -> new GameNotFoundException("Game with id = " + id + " not found"));
    }

    @Override
    public List<Game> getGamesByPublisher(Long publisherId) {
        Publisher publisher = publisherRepo.findById(publisherId).orElseThrow(() -> new PublisherNotFoundException("Publisher with id = " + publisherId + " not found"));
        return gameRepo.findByPublisher(publisher);
    }

    @Override
    public List<Game> getGamesByFilter(GameFilter filter, String value) {
        try {
            switch (filter) {
                case GameFilter.LESS_THEN:
                    return gameRepo.findByPriceLessThanEqual(Integer.parseInt(value));
                case GameFilter.GREATER_THEN:
                    return gameRepo.findByPriceGreaterThanEqual(Integer.parseInt(value));
                case GameFilter.BEFORE:
                    return gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse(value));
                case GameFilter.AFTER:
                    return gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse(value));
                default:
                    throw new GameFilterIsIncorrectException("Filter is incorrect");
            }
        }
        catch (Exception e) {
            throw new GameFilterValueIsIncorrectException("Value is incorrect");
        }
    }
}
