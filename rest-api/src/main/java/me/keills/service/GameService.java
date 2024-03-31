package me.keills.service;

import me.keills.model.Game;
import me.keills.util.GameFilter;

import java.util.List;

public interface GameService {
    void saveGame(Game game);
    void saveGame(Game game,Long publisherId);
    void editGamePrice(Long id, int price);
    void editGamePublisher(Long id, Long publisherId);
    void deleteGame(Long id);
    Game getGame(Long id);
    List<Game> getGamesByPublisher(Long publisherId);
    List<Game> getGamesByFilter(GameFilter filter, String value);
}
