package me.keills.repo;

import io.qameta.allure.*;
import me.keills.model.Game;
import me.keills.model.Publisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayName("Тестирование репозитория игр")
@Severity(SeverityLevel.CRITICAL)
class GameRepoTest {

    @Autowired
    GameRepo gameRepo;

    @Test
    @DisplayName("Поиск игры по имени")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по имени")
    void findByName() {
        Game game = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);

        gameRepo.save(game);

        Game gameFromDatabase = gameRepo.findByName("Super Mario").get();
        assertEquals("Super Mario", gameFromDatabase.getName());
    }

    @Test
    @DisplayName("Поиск игры по издателю")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по издателю")
    void findByPublisher() {
        Publisher publisher = new Publisher("Nintendo", "nin.com");
        Game game = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);
        game.setPublisher(publisher);

        gameRepo.save(game);

        List<Game> gamesFromDatabase = gameRepo.findByPublisher(publisher);
        assertEquals(1, gamesFromDatabase.size());

        Game newGame = new Game("Super Mario 2", LocalDate.parse("2022-01-01"),30);
        newGame.setPublisher(publisher);

        gameRepo.save(newGame);

        gamesFromDatabase = gameRepo.findByPublisher(publisher);
        assertEquals(2, gamesFromDatabase.size());
    }

    @Test
    @DisplayName("Поиск игры по фильтру - раньше чем, для даты релиза")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по фильтру - раньше чем, для даты релиза")
    void findByReleaseDateLessThanEqual() {
        Game game = new Game("Super Mario 2", LocalDate.parse("2022-01-01"),30);
        Game newGame = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);

        gameRepo.save(game);
        gameRepo.save(newGame);

        List<Game> gamesFromDatabase = gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse("2022-01-01"));
        assertEquals(2, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse("2020-01-01"));
        assertEquals(1, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByReleaseDateLessThanEqual(LocalDate.parse("2000-01-01"));
        assertEquals(0, gamesFromDatabase.size());
    }

    @Test
    @DisplayName("Поиск игры по фильтру - позже чем, для даты релиза")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по фильтру - позже чем, для даты релиза")
    void findByReleaseDateGreaterThanEqual() {
        Game game = new Game("Super Mario 2", LocalDate.parse("2022-01-01"),30);
        Game newGame = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);

        gameRepo.save(game);
        gameRepo.save(newGame);

        List<Game> gamesFromDatabase = gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse("2022-01-02"));
        assertEquals(0, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse("2020-01-02"));
        assertEquals(1, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByReleaseDateGreaterThanEqual(LocalDate.parse("2000-01-01"));
        assertEquals(2, gamesFromDatabase.size());
    }

    @Test
    @DisplayName("Поиск игры по фильтру - меньше или равно, для цены")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по фильтру - меньше или равно, для цены")
    void findByPriceLessThanEqual() {
        Game game = new Game("Super Mario 2", LocalDate.parse("2022-01-01"),30);
        Game newGame = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);

        gameRepo.save(game);
        gameRepo.save(newGame);

        List<Game> gamesFromDatabase = gameRepo.findByPriceLessThanEqual(15);
        assertEquals(0, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByPriceLessThanEqual(20);
        assertEquals(1, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByPriceLessThanEqual(30);
        assertEquals(2, gamesFromDatabase.size());
    }

    @Test
    @DisplayName("Поиск игры по фильтру - больше или равно, для цены")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск игры по фильтру - больше или равно, для цены")
    void findByPriceGreaterThanEqual() {
        Game game = new Game("Super Mario 2", LocalDate.parse("2022-01-01"),30);
        Game newGame = new Game("Super Mario", LocalDate.parse("2020-01-01"),20);

        gameRepo.save(game);
        gameRepo.save(newGame);

        List<Game> gamesFromDatabase = gameRepo.findByPriceGreaterThanEqual(20);
        assertEquals(2, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByPriceGreaterThanEqual(30);
        assertEquals(1, gamesFromDatabase.size());
        gamesFromDatabase = gameRepo.findByPriceGreaterThanEqual(35);
        assertEquals(0, gamesFromDatabase.size());
    }
}