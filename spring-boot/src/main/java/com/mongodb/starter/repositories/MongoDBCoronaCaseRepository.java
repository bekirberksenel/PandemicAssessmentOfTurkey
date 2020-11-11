package com.mongodb.starter.repositories;

import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.starter.models.CoronaCase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

@Repository
public class MongoDBCoronaCaseRepository implements CoronaCaseRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    @Autowired
    private MongoClient client;
    private MongoCollection<CoronaCase> caseCollection;

    @PostConstruct
    void init() {
        caseCollection = client.getDatabase("test").getCollection("persons", CoronaCase.class);
    }

    @Override
    public CoronaCase save(CoronaCase person) {
        person.setId(new ObjectId());
        caseCollection.insertOne(person);
        return person;
    }

    @Override
    public List<CoronaCase> saveAll(List<CoronaCase> persons) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                persons.forEach(p -> p.setId(new ObjectId()));
                caseCollection.insertMany(clientSession, persons);
                return persons;
            }, txnOptions);
        }
    }

    @Override
    public List<CoronaCase> findAll() {
        return caseCollection.find().into(new ArrayList<>());
    }
    @Override
    public List<CoronaCase> findAll(String sehir) {
        Bson optionalFilter = eq("city", sehir);

       return  caseCollection.find(optionalFilter).into(new ArrayList<>());

    }


    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> caseCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }
    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }
}
