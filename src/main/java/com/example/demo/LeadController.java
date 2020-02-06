package com.example.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class LeadController {
    @Autowired
    RestTemplate restTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    @CrossOrigin(origins = "*")
    @RequestMapping("/pru")
    public String pru(@RequestParam(value = "name", defaultValue = "Alberto") String name,
                      @RequestParam(value = "mobile", defaultValue = "+6586983689") String mobile,
                      @RequestParam(value = "email", defaultValue = "+6586983689") String email,
                      @RequestParam(value = "agent", defaultValue = "Jird") String agent,
                      @RequestParam(value = "identity", defaultValue = "501182251015") String identity,
                      @RequestParam(value = "clientId", defaultValue = "SUT2") String clientId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic NmMzZWZiYTFmNDM2YmMyMTJmNzU5MzU3M2FlYTIyMDY6M2RjMzI0YWIwMThkZDUzNDdlZjUzYjg2NDcyYjQzMGU");

        HttpEntity<String> entity = new HttpEntity<>("grant_type=password&" +
                "client_id=6c3efba1f436bc212f7593573aea2206&" +
                "username=Nina_Test1&" +
                "password=abcd1234", headers);

        ResponseEntity<String> exchange = restTemplate.exchange("https://pamb.value-ad.com/oauth/token",
                HttpMethod.POST, entity, String.class);

        System.out.println(exchange.getBody());

        JsonNode jsonNode = mapper.readTree(exchange.getBody());
        String access_token = jsonNode.get("access_token").textValue();

        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + access_token);
        headers.add("grant_type", "password");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", dateFormat.format(date));
        jsonObject.put("suppliedClientId", clientId);
        jsonObject.put("firstName", name);
        jsonObject.put("cellNumber", mobile);
        jsonObject.put("emailAddress", email);
        jsonObject.put("identityNumber", identity);
        jsonObject.put("plan", "PRUDengue X");
        jsonObject.put("city", "Singapore");
        jsonObject.put("race", "CHINESE");
        jsonObject.put("languageSpoken", "MANDARIN");
        jsonObject.put("preferredSalesPerson", agent);

        entity = new HttpEntity<>(jsonObject.toString(), headers);

        System.out.println(entity.getBody());

        exchange = restTemplate.exchange("https://pamb.value-ad.com/api/campaigns/49/leads",
                HttpMethod.POST, entity, String.class);

        return exchange.getBody();
    }
}