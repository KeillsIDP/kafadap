package me.keills.service;

import me.keills.KafkaAdapter.payload.Json;
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

@Service
public class HttpMessengerService {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpMessengerService.class);
    public String sendRequest(Json json) {
        try {

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(json.getUrl());

//            if(json.getParameters().size()!=0) {
//                urlBuilder.append("?");
//                for (Map.Entry<String, String> entry : json.getParameters().entrySet())
//                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//            }
//
//            HttpClient httpClient = HttpClient.newHttpClient();
//            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
//                    .method(json.getMethod(), HttpRequest.BodyPublishers.ofString(json.getBody()))
//                    .uri(URI.create(urlBuilder.toString()));
//
//            if(json.getHeaders().size()!=0) {
//                for (Map.Entry<String, List<String>> entry : json.getHeaders().entrySet())
//                    httpRequestBuilder.setHeader(entry.getKey(), entry.getValue().toString());
//            }
//
//            HttpRequest httpRequest = httpRequestBuilder.build();
//            HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//            return (String) httpResponse.body();
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
                LOGGER.info("#### -> HTTP response ->" + response.toString());
                httpURLConnection.disconnect();
            }
            return response.toString();
        } catch (IOException e) {
            LOGGER.error("#### -> ERROR with HTTP request -> " + e.getMessage());

        }
//        catch (InterruptedException e) {
//            LOGGER.error("#### -> ERROR with HTTP request -> " + e.getMessage());
//        }
        return "";
    }
}
