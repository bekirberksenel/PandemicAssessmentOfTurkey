package com.mongodb.starter.controllers;

import com.mongodb.starter.HaberParser;
import com.mongodb.starter.models.CaseSummary;
import com.mongodb.starter.models.CoronaCase;
import com.mongodb.starter.repositories.CoronaCaseRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CoronaCaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CoronaCaseController.class);
    private final CoronaCaseRepository coronaCaseRepository;

    public CoronaCaseController(CoronaCaseRepository coronaCaseRepository) {
        this.coronaCaseRepository = coronaCaseRepository;
    }

    //Declaring rooting for method
    @PostMapping("insertCase")
    @ResponseStatus(HttpStatus.CREATED)
    //ALLOW react to access method
    @CrossOrigin(origins = "http://localhost:3000")
    public CoronaCase postCase(@RequestBody String haber) {
        HaberParser parser = new HaberParser(haber);

        try{
            //try to parse corona news
            CoronaCase c = parser.getCase();
            if(c == null){
                c = new CoronaCase();
                return c;
            };
            // save to db if valid new
            return coronaCaseRepository.save(c);

        }catch (Exception e){

            System.out.println(e);
            e.printStackTrace();
            CoronaCase c =  new CoronaCase();
                return c;

        }
    }

    @PostMapping("caseSummary")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:3000")

    public ArrayList<CaseSummary> getSummary(@RequestBody String sehir) {
        try{
            HashMap<String,CaseSummary> summaries = new HashMap<String,CaseSummary>();
            List<CoronaCase> cases;
            // find corona cases from db
            if(sehir .equals("TÜM ŞEHİRLER"))
             cases = coronaCaseRepository.findAll();
            else
                cases = coronaCaseRepository.findAll(sehir);

            //CALCULATE SUMMARY FOR ALL CASE DATES
            for(int a=0;a<cases.size();a++){

                // ıf summary for date exist, add to it
                if(summaries.containsKey(cases.get(a).getDate().toString())){
                    CaseSummary s = summaries.get(cases.get(a).getDate().toString());
                    s.toplamTaburcu += cases.get(a).getTaburcuSayisi();
                    s.toplamVaka += cases.get(a).getVakaSayisi();
                    s.toplamVefat += cases.get(a).getVefatSayisi();
                }else{
                    // create new summary if not exist for date
                    CaseSummary s = new CaseSummary(sehir,cases.get(a).getDate());
                    s.toplamTaburcu += cases.get(a).getTaburcuSayisi();
                    s.toplamVaka += cases.get(a).getVakaSayisi();
                    s.toplamVefat += cases.get(a).getVefatSayisi();
                    summaries.put(cases.get(a).getDate().toString(),s);
                }
            }
            // GET SUMMARRİES AND SOORT
            ArrayList<CaseSummary> listOfKeys
                    = new ArrayList<CaseSummary>(summaries.values());

            listOfKeys.sort((o1, o2) -> o1.date.compareTo(o2.date));

            return listOfKeys;

        }catch (Exception e){

            System.out.println(e);
            e.printStackTrace();
            return null;

        }
    }

    @GetMapping("cases")
    public List<CoronaCase> getCases() {
        return coronaCaseRepository.findAll();
    }


    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}
