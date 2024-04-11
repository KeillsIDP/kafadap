package me.keills.service;

import me.keills.model.Game;
import me.keills.util.GameFilter;

import java.util.List;

/**
 * Сервисный интерфейс для работы с играми.
 */
public interface GameService {
    /**
     * Сохранить игру.
     * @param game {@link Game} - объект игры для сохранения
     * @return {@link Game} сохраненный объект игры
     */
    Game saveGame(Game game);

    /**
     * Сохранить игру с указанием идентификатора издателя.
     * @param game {@link Game} - объект игры для сохранения
     * @param publisherId {@link Long} - идентификатор издателя
     * @return {@link Game} сохраненный объект игры
     */
    Game saveGame(Game game, Long publisherId);

    /**
     * Изменить цену игры.
     * @param id {@link Long} - идентификатор игры
     * @param price {@link Integer} - новая цена игры
     * @return {@link Game} измененный объект игры
     */
    Game editGamePrice(Long id, int price);

    /**
     * Изменить издателя игры.
     * @param id {@link Long} - идентификатор игры
     * @param publisherId {@link Long} - идентификатор нового издателя
     * @return {@link Game} измененный объект игры
     */
    Game editGamePublisher(Long id, Long publisherId);

    /**
     * Удалить игру по идентификатору.
     * @param id {@link Long} - идентификатор игры для удаления
     */
    void deleteGame(Long id);

    /**
     * Получить игру по идентификатору.
     * @param id {@link Long} - идентификатор игры
     * @return {@link Game} объект игры с указанным идентификатором
     */
    Game getGame(Long id);

    /**
     * Получить список игр по идентификатору издателя.
     * @param publisherId {@link Long} - идентификатор издателя
     * @return список игр, связанных с указанным издателем
     */
    List<Game> getGamesByPublisher(Long publisherId);

    /**
     * Получить список игр по фильтру и значению.
     * @param filter {@link GameFilter} - фильтр для поиска игр
     * @param value {@link String} - значение для фильтрации
     * @return список игр, соответствующих фильтру и значению
     */
    List<Game> getGamesByFilter(GameFilter filter, String value);
}
