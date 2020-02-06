package com.example.demo;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;


@RestController
public class Controller {
    @Autowired
    RestTemplate restTemplate;

    @CrossOrigin(origins = "*")
    @GetMapping("/t")
    public String translate(@RequestParam(value = "text", defaultValue = "World") String text,
                            @RequestParam(value = "lang", defaultValue = "en-zh") String lang) {

        String auth = "apiKey:tf5Itn281M_QJvAV6ZjxX-qSJc6QCYvwlCHsWuc87MjM";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authHeader);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(text);
        jsonObject.put("text", jsonArray);
        jsonObject.put("model_id", lang);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> exchange = restTemplate.exchange(
                "https://api.us-south.language-translator.watson.cloud.ibm.com/instances/705c1313-c90d-4623-b89f-da41325f2ab9/v3/translate?version=2018-05-01",
                HttpMethod.POST, entity, String.class);

        return exchange.getBody();
    }

    @GetMapping("/r")
    public String random(@RequestParam(value = "text", defaultValue = "World") String text) {
        String response = restTemplate.getForObject(
                "https://gturnquist-quoters.cfapps.io/api/random", String.class);
        return response;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    private LeadRepositiory repositiory;

    @GetMapping("/api/lead")
    public List<LeadModel> get() {
        return this.repositiory.findAll();
    }

    @PostMapping("/api/lead")
    public LeadModel insert(@RequestBody final LeadModel model) {
        return this.repositiory.save(model);
    }
}