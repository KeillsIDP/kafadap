package me.keills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import me.keills.exception.game.GameFilterIsIncorrectException;
import me.keills.exception.game.GameNotFoundException;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Game;
import me.keills.model.Publisher;
import me.keills.service.GameService;
import me.keills.util.GameFilter;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование GameController")
@Feature("Game Controller")
@Story("Сохранение игры")
@Severity(SeverityLevel.CRITICAL)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Сохранение игры в формате JSON")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры в формате JSON")
    void saveGame_Json() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        ResultActions response = mockMvc.perform(post("/api/game/save-json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Сохранение игры в формате JSON с некорректными данными")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры в формате JSON с некорректными данными")
    void saveGame_Json_Incorrect() throws Exception {
        Game game = new Game();

        ResultActions response = mockMvc.perform(post("/api/game/save-json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Сохранение игры по параметрам")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры по параметрам")
    void saveGame_Parameters() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        ResultActions response = mockMvc.perform(post("/api/game/save")
                .param("gameName",game.getName())
                .param("releaseDate",game.getReleaseDate().toString())
                .param("price",game.getPrice()+""));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Сохранение игры по параметрам с некорректной датой релиза")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры по параметрам с указанием неверной даты")
    void saveGame_Parameters_Incorrect_ReleaseDate() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        ResultActions response = mockMvc.perform(post("/api/game/save")
                .param("gameName",game.getName())
                .param("releaseDate","asd")
                .param("price",game.getPrice()+""));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Сохранение игры по параметрам с некорректной ценой")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры по параметрам с указанием неверной цены")
    void saveGame_Parameters_Incorrect_Price() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);

        ResultActions response = mockMvc.perform(post("/api/game/save")
                .param("gameName",game.getName())
                .param("releaseDate",game.getReleaseDate().toString())
                .param("price","asd"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Сохранение игры по параметрам с некорректным издателем")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение игры по параметрам с указанием неверного издателя")
    void saveGame_Parameters_PublisherNotFound() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameService.saveGame(ArgumentMatchers.any(),ArgumentMatchers.anyLong())).thenThrow(new PublisherNotFoundException("Publisher not found"));
        ResultActions response = mockMvc.perform(post("/api/game/save")
                .param("gameName",game.getName())
                .param("releaseDate",game.getReleaseDate().toString())
                .param("price","asd")
                .param("publisherId","1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Изменение цены на игру")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение цены на игру")
    void editGamePrice() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/game/edit")
                .param("id","1")
                .param("price","15"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Изменение цены на игру при неверной цене")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение цены на игру с указанием неверной цены")
    void editGamePrice_Incorrect_Price() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/game/edit")
                .param("id","1")
                .param("price","asd"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Изменение цены на игру, которая не существует")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение цены на игру, но при этом игра не сохранена в базе данных")
    void editGamePrice_GameNotFound() throws Exception {
        when(gameService.editGamePrice(ArgumentMatchers.anyLong(),ArgumentMatchers.anyInt())).thenThrow(new GameNotFoundException("Game not found"));
        ResultActions response = mockMvc.perform(post("/api/game/edit")
                .param("id","1")
                .param("price","15"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Изменение издателя игры")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение издателя игры")
    void editGamePublisher() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/game/edit-publisher")
            .param("id","1")
            .param("publisherId","1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Изменение издателя игры, при том что он не сохранен в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение издателя игры, если присваиваемый издатель не сохранен в базе данных")
    void editGamePublisher_PublisherNotFound() throws Exception {
        when(gameService.editGamePublisher(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())).thenThrow(new PublisherNotFoundException("Publisher not found"));
        ResultActions response = mockMvc.perform(post("/api/game/edit-publisher")
                .param("id","1")
                .param("publisherId","1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Изменение издателя игры, при том что игра не сохранена в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет изменение издателя игры, если игра не сохранена в базе данных")
    void editGamePublisher_GameNotFound() throws Exception {
        when(gameService.editGamePublisher(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())).thenThrow(new GameNotFoundException("Game not found"));
        ResultActions response = mockMvc.perform(post("/api/game/edit-publisher")
                .param("id","1")
                .param("publisherId","1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получить объект игры из базы данных по идентификатору")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение объекта игры из базы данных по идентификатору")
    void getGame() throws Exception {
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        when(gameService.getGame(ArgumentMatchers.anyLong())).thenReturn(game);

        ResultActions response = mockMvc.perform(get("/api/game/get")
                .param("id","1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(game.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate", CoreMatchers.is(game.getReleaseDate().toString())));
    }

    @Test
    @DisplayName("Получить объект игры из базы данных по идентификатору, при том что игра не сохранена в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение объекта игры из базы данных по идентификатору, если игра не сохранена в базе данных")
    void getGame_GameNotFound() throws Exception {
        when(gameService.getGame(ArgumentMatchers.anyLong())).thenThrow(new GameNotFoundException("Game not found"));

        ResultActions response = mockMvc.perform(get("/api/game/get")
                .param("id","1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получить игры по издателю")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры по издателю")
    void getGamesByPublisher() throws Exception {
        Publisher publisher = new Publisher("publisher","/");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        game.setPublisher(publisher);

        when(gameService.getGamesByPublisher(ArgumentMatchers.anyLong())).thenReturn(List.of(game));

        ResultActions response = mockMvc.perform(get("/api/game/get-by-publisher")
                .param("id","1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(game.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate", CoreMatchers.is(game.getReleaseDate().toString())));
    }

    @Test
    @DisplayName("Получить игры по издателю, при том что издатель не сохранен в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры по издателю, если издатель не сохранен в базе данных")
    void getGamesByPublisher_PublisherNotFound() throws Exception {
        when(gameService.getGamesByPublisher(ArgumentMatchers.anyLong())).thenThrow(new PublisherNotFoundException("Publisher not found"));
        ResultActions response = mockMvc.perform(get("/api/game/get-by-publisher")
                .param("id","1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получить игры по фильтру")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры по фильтру")
    void getGamesByFilter() throws Exception {
        Publisher publisher = new Publisher("publisher","/");
        Game game = new Game("game", LocalDate.parse("2020-01-01"), 10);
        game.setPublisher(publisher);

        when(gameService.getGamesByFilter(ArgumentMatchers.any(GameFilter.class),ArgumentMatchers.anyString())).thenReturn(List.of(game));

        ResultActions response = mockMvc.perform(get("/api/game/get-by-filter")
                .param("filter","AFTER")
                .param("value","2020-01-01"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(game.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseDate", CoreMatchers.is(game.getReleaseDate().toString())));
    }

    @Test
    @DisplayName("Получить игры по фильтру, при том что фильтр некорректен")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры по фильтру, при том что фильтр некорректен")
    void getGamesByFilter_IncorrectFilter() throws Exception {
        when(gameService.getGamesByFilter(ArgumentMatchers.any(GameFilter.class),ArgumentMatchers.anyString())).thenThrow(new GameFilterIsIncorrectException("Filter is incorrect"));

        ResultActions response = mockMvc.perform(get("/api/game/get-by-filter")
                .param("filter","AFTER")
                .param("value","2020-01-01"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получить игры по фильтру, при том что значение фильтра некорректно")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение игры по фильтру, при том что значение фильтра некорректно")
    void getGamesByFilter_IncorrectValue() throws Exception {
        when(gameService.getGamesByFilter(ArgumentMatchers.any(GameFilter.class),ArgumentMatchers.anyString())).thenThrow(new GameFilterIsIncorrectException("Filter is incorrect"));

        ResultActions response = mockMvc.perform(get("/api/game/get-by-filter")
                .param("filter","AFTER")
                .param("value","asd"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}