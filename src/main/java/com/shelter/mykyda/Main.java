package com.shelter.mykyda;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer BQD37QuuHxf5gOMffBEN-vEg1kX3MV9CuGWLa4gDsS06Nz3q1HEMJpCwS43J3hG2Nr4ok6zSORvf9LQfE2_3QFUF0Rs5ofNWXv0XITEbVuRPV6mbzN88UH7RzxWWjQbWWHkRoGvulIJB0gXY59JGV8gs9k2m9G5BnYB0v1-LKTe0-qc3cLtU7kvl16cdt51-AHZeqwQRfjpa1b_L1kbv5fcgXnF6zCQfdI6Fawk6Vl6HQd2XfGY7qtuOcVRMYQ9AaYdt93xkuehoz7l7Jwh2IZcV4UuD5fHvkXYUImCeXc5va-XgWKOEDxD6-ML5");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://api.spotify.com/v1/me/top/artists?time_range=short_term&limit=30", HttpMethod.GET, entity, Map.class);
        Map body = response.getBody();
        List<Map> items = (List<Map>) body.get("items");
        for (Map item : items) {
            String artistName = (String) item.get("name");
            System.out.println(artistName);
        }
        System.out.println(items);
    }
}
