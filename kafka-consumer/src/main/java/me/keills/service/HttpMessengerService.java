package me.keills.service;

import me.keills.payload.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * Сервис для отправки HTTP-запросов.
 */
@Service
public class HttpMessengerService {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpMessengerService.class);

    /**
     * Метод для отправки HTTP-запроса на указанный URL с заданными параметрами, заголовками и телом.
     * @param json {@link Json} объект с информацией о запросе (URL, параметры, заголовки, тело и метод).
     * @return {@link String} с ответом от сервера или "" в случае ошибки.
     */
    public String sendRequest(Json json) {
        try {

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(json.getUrl());

            if(json.getParameters().size()!=0) {
                urlBuilder.append("?");
                for (Map.Entry<String, String> entry : json.getParameters().entrySet())
                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(json.getMethod());

            if(json.getHeaders().size()!=0) {
                for (Map.Entry<String, List<String>> entry : json.getHeaders().entrySet()) {
                    System.out.println(entry.getKey() + " " + entry.getValue());
                    httpURLConnection.setRequestProperty(entry.getKey(), String.join(";", entry.getValue()));
                }
            }

            if(json.getBody().length()!=0){
                httpURLConnection.setDoOutput(true);
                try(OutputStream os = httpURLConnection.getOutputStream()) {
                    byte[] input = json.getBody().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            LOGGER.info(String.format("#### -> HTTP request -> METHOD:%s  |||  HEADERS:%s  |||  URL:%s",
                    httpURLConnection.getRequestMethod(),
                    httpURLConnection.getHeaderFields().toString(),
                    httpURLConnection.getURL()
            ));

            StringBuilder response = new StringBuilder();
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                LOGGER.info("#### -> HTTP response ->" + response);
                httpURLConnection.disconnect();
            }
            return response.toString();
        } catch (IOException e) {
            LOGGER.error("#### -> ERROR with HTTP request -> " + e.getMessage());

        }
        return "";
    }
}
