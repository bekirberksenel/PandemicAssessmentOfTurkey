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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HaberParserTest {



        
    @DisplayName("PTest haber parser")
    @Test
    void testHaberParser() {
        HaberParser parser = new HaberParser("20.08.2020 tarihinde Ankara da Korona virüs salgınında yapılan testlerde\n" +
                "15 yeni vaka bulundu. 1 kişi korona dan vefat etti. 5 kişide taburcu oldu.\n");
        CoronaCase case1= parser.getCase();
        assertThat(case1.getTaburcuSayisi()).isEqualTo(5);
        assertThat(case1.getVefatSayisi()).isEqualTo(1);
        assertThat(case1.getVakaSayisi()).isEqualTo(15);
        assertThat(case1.getDate()).isEqualTo("2020-08-20");
        assertThat(case1.getCity()).isEqualTo("Ankara");

        parser = new HaberParser(": Korona virüs salgınında yapılan testlerde 19.08.2020 tarihinde İstanbul da\n" +
                "30 yeni vaka tespit edil. İstanbul da taburcu sayısı 7 kişi . 3 kişi de vefat etti.");
        CoronaCase case2= parser.getCase();
        assertThat(case2.getTaburcuSayisi()).isEqualTo(7);
        assertThat(case2.getVefatSayisi()).isEqualTo(3);
        assertThat(case2.getVakaSayisi()).isEqualTo(30);
        assertThat(case2.getDate()).isEqualTo("2020-08-19");
        assertThat(case2.getCity()).isEqualTo("İstanbul");

        parser = new HaberParser("19.08.2020 tarihinde İstanbul için korona virüs ile ilgili yeni bir açıklama\n" +
                "yapıldı. Korona virüs salgınında yapılan testlerde 20 yeni vaka tespit edildi. taburcu sayısı ise\n" +
                "7 oldu. 3 kişin de vefat ettiği öğrenildi");
        CoronaCase case3= parser.getCase();
        assertThat(case3.getTaburcuSayisi()).isEqualTo(7);
        assertThat(case3.getVefatSayisi()).isEqualTo(3);
        assertThat(case3.getVakaSayisi()).isEqualTo(20);
        assertThat(case3.getDate()).isEqualTo("2020-08-19");
        assertThat(case3.getCity()).isEqualTo("İstanbul");


    }

}
