package me.keills.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import io.qameta.allure.*;
import me.keills.payload.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableWireMock({
        @ConfigureWireMock(name = "test-client", property = "test.url", stubLocation = "custom-location")
})
@DisplayName("Тестирование сервиса мессенджера HTTP")
@Feature("HTTP")
@Story("Отправка запросов")
@Severity(SeverityLevel.NORMAL)
class HttpMessengerServiceTest {
    @InjectWireMock("test-client")
    private WireMockServer wireMockServer;
    @Autowired
    private Environment env;
    private HttpMessengerService httpMessengerService = new HttpMessengerService();
    @Test
    @DisplayName("Отправка запроса")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет отправку запроса и получение ответа 'OK'")
    void sendRequest() throws IOException {
        // given
        String url = env.getProperty("test.url")+"/";
        Json request = new Json("GET",url, "", new HashMap<>(), new HashMap<>());

        wireMockServer.stubFor(get("/")
                .willReturn(aResponse().withBody("OK")));

        String response = httpMessengerService.sendRequest(request);
        // then
        assertEquals("OK",response);
    }

    @Test
    @DisplayName("Отправка запроса с некорректной ссылкой")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет отправку запроса с некорректной ссылкой и получение пустого ответа")
    void sendRequestWithIncorrectLink() throws IOException {
        // given
        String url = env.getProperty("test.url")+"/a";
        Json request = new Json("GET",url, "", new HashMap<>(), new HashMap<>());

        wireMockServer.stubFor(get("/")
                .willReturn(aResponse().withBody("OK")));

        String response = httpMessengerService.sendRequest(request);
        // then
        assertEquals("",response);
    }

    @Test
    @DisplayName("Отправка запроса с некорректным типом метода")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет отправку запроса с некорректным типом метода и получение пустого ответа")
    void sendRequestWithIncorrectMethodType() throws IOException {
        // given
        String url = env.getProperty("test.url")+"/";
        Json request = new Json("GET",url, "{}", new HashMap<>(), new HashMap<>());

        wireMockServer.stubFor(get("/")
                .willReturn(aResponse().withBody("OK")));

        String response = httpMessengerService.sendRequest(request);
        // then
        assertEquals("",response);
    }

    @Test
    @DisplayName("Отправка запроса с параметрами")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет отправку запроса с параметрами и получение корректного ответа")
    void sendRequestWithParameters() throws IOException {
        // given
        String url = env.getProperty("test.url")+"/";
        String value = "value";
        Json request = new Json("POST",url, "", new HashMap<>(), Map.of("key",value));

        wireMockServer.stubFor(post("/?key="+value)
                .willReturn(aResponse().withBody(value)));

        String response = httpMessengerService.sendRequest(request);
        // then
        assertEquals(value,response);
    }

    @Test
    @DisplayName("Отправка запроса с заголовками")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет отправку запроса с заголовками и получение корректного ответа")
    void sendRequestWithHeaders() throws IOException {
        // given
        String url = env.getProperty("test.url")+"/";
        Json request = new Json("POST",url, "", Map.of("Content-Type",List.of("application/json")), new HashMap<>());

        wireMockServer.stubFor(post("/")
                        .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse().withBody("{\"name\":\"test\"}")));

        String response = httpMessengerService.sendRequest(request);
        // then
        assertEquals("{\"name\":\"test\"}",response);
    }
}