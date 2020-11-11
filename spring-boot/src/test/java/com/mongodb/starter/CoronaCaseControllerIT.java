package com.mongodb.starter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.starter.models.CoronaCase;
import com.mongodb.starter.repositories.CoronaCaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoronaCaseControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private CoronaCaseRepository coronaCaseRepository;
    @Autowired
    private TestHelper testHelper;
    private String URL;

    @Autowired
    CoronaCaseControllerIT(MongoClient mongoClient) {
        createPersonCollectionIfNotPresent(mongoClient);
    }

    @PostConstruct
    void setUp() {
        URL = "http://localhost:" + port + "/api";
    }

    @AfterEach
    void tearDown() {
        coronaCaseRepository.deleteAll();
    }

    @DisplayName("POST /case with 1 case")
    @Test
    void postCase() {
        // GIVEN
        // WHEN
        ResponseEntity<CoronaCase> result = rest.postForEntity(URL + "/insertCase", "20.08.2020 tarihinde Ankara da Korona virüs salgınında yapılan testlerde\n" +
                "15 yeni vaka bulundu. 1 kişi korona dan vefat etti. 5 kişide taburcu oldu.", CoronaCase.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CoronaCase personResult = result.getBody();
        assertThat(personResult.getId()).isNotNull();
        assertThat(personResult.getTaburcuSayisi()).isEqualTo(5);
    }


    @DisplayName("GET /persons with 1 case")
    @Test
    void getCases() {
        // GIVEN
        List<CoronaCase> personsInserted = coronaCaseRepository.saveAll(testHelper.getCoronaCases());
        // WHEN
        ResponseEntity<List<CoronaCase>> result = rest.exchange(URL + "/cases", HttpMethod.GET, null,
                                                            new ParameterizedTypeReference<List<CoronaCase>>() {
                                                            });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private void createPersonCollectionIfNotPresent(MongoClient mongoClient) {
        // This is required because it is not possible to create a new collection within a multi-documents transaction.
        // Some tests start by inserting 2 documents with a transaction.
        MongoDatabase db = mongoClient.getDatabase("test");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("persons"))
            db.createCollection("persons");
    }
}
