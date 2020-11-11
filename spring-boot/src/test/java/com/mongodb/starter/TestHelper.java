package com.mongodb.starter;

import com.mongodb.starter.models.CoronaCase;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
class TestHelper {

    CoronaCase getCase1() {
        return new CoronaCase();
    }

    CoronaCase getCase2() {
        return new CoronaCase();
    }

    List<CoronaCase> getCoronaCases() {
        return asList(getCase1(), getCase2());
    }
}
