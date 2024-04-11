package me.keills.service;

import io.qameta.allure.*;
import me.keills.exception.game.GameFilterIsIncorrectException;
import me.keills.exception.game.GameFilterValueIsIncorrectException;
import me.keills.exception.game.GameNotFoundException;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Game;
import me.keills.model.Publisher;
import me.keills.repo.GameRepo;
import me.keills.repo.PublisherRepo;
import me.keills.util.GameFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Feature("Игры")
@Story("Сервисный интерфейс для работы с играми")
@Severity(SeverityLevel.CRITICAL)
class GameServiceTest {
    @Mock
    GameRepo gameRepo;
    @Mock
    PublisherRepo publisherRepo;
    @InjectMocks
    GameServiceImpl gameService;
    @Test
    @DisplayName("Сохранение игры")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры")
    void saveGame() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameRepo.save(game)).thenReturn(game);
        assertEquals(game, gameService.saveGame(game));
    }

    @Test
    @DisplayName("Сохранение игры с издателем")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры с издателем")
    void saveGame_withPublisher_PublisherExist() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        game.setPublisher(publisher);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(publisher));
        when(gameRepo.save(game)).thenReturn(game);
        assertEquals(game, gameService.saveGame(game,1l));
        assertEquals(publisher, game.getPublisher());
    }

    @Test
    @DisplayName("Сохранение игры с издателем")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры с несуществующим издателем")
    void saveGame_withPublisher_PublisherNotExist_PublisherNotFoundException() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        game.setPublisher(publisher);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(PublisherNotFoundException.class, () -> gameService.saveGame(game,1l));
    }

    @Test
    @DisplayName("Редактирование цены")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет редактирование цены")
    void editGamePrice_GameExist() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(game));
        when(gameRepo.save(game)).thenReturn(game);
        assertEquals(15, gameService.editGamePrice(1l,15).getPrice());
    }

    @Test
    @DisplayName("Редактирование цены")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Попытка редактирования несуществующей игры")
    void editGamePrice_GameNotExist_GameNotFoundException() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(null));
        assertThrows(GameNotFoundException.class, () -> gameService.editGamePrice(1l,15).getPrice());
    }

    @Test
    @DisplayName("Редактирование издателя")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет редактирование издателя")
    void editGamePublisher_PublisherExist() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(publisher));
        when(gameRepo.save(game)).thenReturn(game);
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(game));
        assertEquals(publisher, gameService.editGamePublisher(1l,1l).getPublisher());
    }

    @Test
    @DisplayName("Редактирование издателя")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Попытка редактирования несуществующего издателя")
    void editGamePublisher_PublisherNotExist_PublisherNotFoundException() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(null));
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(game));
        assertThrows(PublisherNotFoundException.class, () -> gameService.editGamePublisher(1l,1l));
    }

    @Test
    @DisplayName("Редактирование издателя")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Попытка редактирования несуществующей игры")
    void editGamePublisher_GameNotExist_GameNotFoundException() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(null));
        assertThrows(GameNotFoundException.class, () -> gameService.editGamePublisher(1l,1l));
    }

    @Test
    @DisplayName("Получение игры")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры")
    void getGame_Exist() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(game));
        assertEquals(game, gameService.getGame(1l));
    }

    @Test
    @DisplayName("Получение игры")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Попытка получения несуществующей игры")
    void getGame_NotExist_GameNotFoundException() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameRepo.findById(1l)).thenReturn(Optional.ofNullable(null));
        assertThrows(GameNotFoundException.class, () ->gameService.getGame(1l));
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByPublisher_Exist_NoGamesFromPublisher() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(publisher));
        when(gameRepo.findByPublisher(publisher)).thenReturn(List.of());
        assertEquals(0,gameService.getGamesByPublisher(1l).size());
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByPublisher_Exist_OneGameFromPublisher() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        game.setPublisher(publisher);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(publisher));
        when(gameRepo.findByPublisher(publisher)).thenReturn(List.of(game));
        assertEquals(1,gameService.getGamesByPublisher(1l).size());
    }


    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Попытка получения несуществующего издателя")
    void getGamesByPublisher_NotExist_PublisherNotFoundException() {
        Publisher publisher = new Publisher("publisher","publisher");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(PublisherNotFoundException.class, () -> gameService.getGamesByPublisher(1l));
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_LessThen() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(gameRepo.findByPriceLessThanEqual(15)).thenReturn(List.of(game));
        assertEquals(1,gameService.getGamesByFilter(GameFilter.LESS_THEN,"15").size());
        when(gameRepo.findByPriceLessThanEqual(5)).thenReturn(List.of());
        assertEquals(0,gameService.getGamesByFilter(GameFilter.LESS_THEN,"5").size());
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_GreaterThen() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(gameRepo.findByPriceGreaterThanEqual(5)).thenReturn(List.of(game));
        assertEquals(1,gameService.getGamesByFilter(GameFilter.GREATER_THEN,"5").size());
        when(gameRepo.findByPriceGreaterThanEqual(15)).thenReturn(List.of());
        assertEquals(0,gameService.getGamesByFilter(GameFilter.GREATER_THEN,"15").size());
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_After() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse("2020-01-01"))).thenReturn(List.of(game));
        assertEquals(1,gameService.getGamesByFilter(GameFilter.AFTER,"2020-01-01").size());
        when(gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse("2020-01-02"))).thenReturn(List.of());
        assertEquals(0,gameService.getGamesByFilter(GameFilter.AFTER,"2020-01-02").size());
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_Before() {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        when(gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse("2019-01-01"))).thenReturn(List.of());
        assertEquals(0,gameService.getGamesByFilter(GameFilter.BEFORE,"2019-01-01").size());
        when(gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse("2021-01-02"))).thenReturn(List.of(game));
        assertEquals(1,gameService.getGamesByFilter(GameFilter.BEFORE,"2021-01-02").size());
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_GameFilterIsIncorrectException() {
        assertThrows(GameFilterIsIncorrectException.class, () -> gameService.getGamesByFilter(null,"5"));
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_GameFilterValueIsIncorrectException_ForDate() {
        assertThrows(GameFilterValueIsIncorrectException.class, () -> gameService.getGamesByFilter(GameFilter.BEFORE,"5"));
    }

    @Test
    @DisplayName("Получение игр")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игр")
    void getGamesByFilter_GameFilterValueIsIncorrectException_ForPrice() {
        assertThrows(GameFilterValueIsIncorrectException.class, () -> gameService.getGamesByFilter(GameFilter.GREATER_THEN,"5a"));
    }

}