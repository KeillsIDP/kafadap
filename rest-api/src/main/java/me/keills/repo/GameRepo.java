package me.keills.repo;

import me.keills.model.Game;
import me.keills.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с играми в базе данных.
 */
@Repository
public interface GameRepo extends JpaRepository<Game,Long> {
    /**
     * Найти игру по имени.
     * @param name {@link String} - имя игры
     * @return {@link Optional} с объектом игры, если найден, иначе пустой Optional
     */
    Optional<Game> findByName(String name);
    /**
     * Найти список игр по издателю.
     * @param publisher {@link Publisher} - объект издателя
     * @return список игр, связанных с указанным издателем
     */
    List<Game> findByPublisher(Publisher publisher);
    /**
     * Найти список игр с датой выпуска не позже указанной даты.
     * @param date {@link LocalDate} дата, до которой были выпущены игры
     * @return список игр, с датой выпуска не позже указанной даты
     */
    List<Game> findByReleaseDateLessThanEqual(LocalDate date);
    /**
     * Найти список игр с датой выпуска не раньше указанной даты.
     * @param date {@link LocalDate} - дата, после которой были выпущены игры
     * @return список игр с датой выпуска не раньше указанной даты
     */
    List<Game> findByReleaseDateGreaterThanEqual(LocalDate date);
    /**
     * Найти список игр с ценой не выше указанной.
     * @param price {@link Integer} - максимальная цена игры
     * @return с ценой не выше указанной
     */
    List<Game> findByPriceLessThanEqual(Integer price);
    /**
     * Найти список игр с ценой не ниже указанной.
     * @param price {@link Integer} - минимальная цена игры
     * @return список игр с ценой не ниже указанной
     */
    List<Game> findByPriceGreaterThanEqual(Integer price);
}
