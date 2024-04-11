package me.keills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import me.keills.exception.publisher.PublisherNameNotPresent;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Publisher;
import me.keills.service.PublisherService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = PublisherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PublisherService publisherService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Сохранение издателя в формате JSON")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя в формате JSON")
    void savePublisher_Json() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        ResultActions response = mockMvc.perform(post("/api/publisher/save-json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publisher)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Сохранение издателя в формате JSON с отсутствием имени")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя в формате JSON с отсутствием имени")
    void savePublisher_Json_PublisherNameNotPresent() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        when(publisherService.savePublisher(ArgumentMatchers.any(Publisher.class))).thenThrow(new PublisherNameNotPresent("Publisher name can't be empty"));
        ResultActions response = mockMvc.perform(post("/api/publisher/save-json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publisher)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Сохранение издателя в формате JSON с некорректными данными")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя в формате JSON с некорректными данными")
    void savePublisher() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        ResultActions response = mockMvc.perform(post("/api/publisher/save")
                .param("name", publisher.getName())
                .param("url", publisher.getUrl()));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Сохранение издателя в формате JSON с отсутствием имени")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя в формате JSON с отсутствием имени")
    void savePublisher_PublisherNameNotPresent() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        when(publisherService.savePublisher(ArgumentMatchers.any(Publisher.class))).thenThrow(new PublisherNameNotPresent("Publisher name can't be empty"));
        ResultActions response = mockMvc.perform(post("/api/publisher/save")
                .param("name", publisher.getName())
                .param("url", publisher.getUrl()));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получение издателя по ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение издателя по ID")
    void getPublisher() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        when(publisherService.getPublisherById(ArgumentMatchers.anyLong())).thenReturn(publisher);
        ResultActions response = mockMvc.perform(get("/api/publisher/get")
                .param("id", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(publisher.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", CoreMatchers.is(publisher.getUrl())));
    }

    @Test
    @DisplayName("Получение издателя по ID с некорректным ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение издателя по ID с некорректным ID")
    void getPublisher_PublisherNotFound() throws Exception {
        Publisher publisher = new Publisher("publisher", "url");
        when(publisherService.getPublisherById(ArgumentMatchers.anyLong())).thenThrow(new PublisherNotFoundException("Publisher not found"));
        ResultActions response = mockMvc.perform(get("/api/publisher/get")
                .param("id", "1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}