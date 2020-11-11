package com.mongodb.starter.repositories;

import com.mongodb.starter.models.CoronaCase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoronaCaseRepository {

    CoronaCase save(CoronaCase person);

    List<CoronaCase> saveAll(List<CoronaCase> persons);

    List<CoronaCase> findAll();

    List<CoronaCase> findAll(String sehir);




    long deleteAll();



}
