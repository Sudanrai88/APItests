package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestApiTut {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        //Post request
        Transcript transcript = new Transcript();
        // ?raw=true
        transcript.setAudio_url("https://github.com/Sudanrai88/AssemblyAi/blob/main/AudioFiles/Recording%20(2).m4a?raw=true");
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

        System.out.println(jsonRequest);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization","8cdc2f66fe414185a309d5052018bcfb")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient2 = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient2.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization","8cdc2f66fe414185a309d5052018bcfb")
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient2.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            System.out.println(transcript.getStatus());

            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            }
            Thread.sleep(1000);
        }

        System.out.println("Transcription completed");
        System.out.println(transcript.getText());










    }
}