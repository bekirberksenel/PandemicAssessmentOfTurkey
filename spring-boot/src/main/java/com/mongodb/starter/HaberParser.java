package com.mongodb.starter;


import com.mongodb.starter.models.CoronaCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class HaberParser {

    private String text;
    private String[] iller = new String[]{"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir", "Bilecik",
            "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
            "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun",
            "Gümüşhane", "Hakkari", "Hatay", "Isparta", "İçel (Mersin)", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
            "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "K.maraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray",
            "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};
    public HaberParser(String text){
        this.text = text;
    }

    // REMOVE DATE FROM STRİNG TO DONT MESS WHEN SPLİT CUMLE ON  DOT (.)
    private String dateDeletedString(String cumle){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String date = this.getDate().format(formatter);
        return cumle.replace(date,"");
    }
    public CoronaCase getCase(){
        LocalDate date = getDate();
        String il = getIl();
        if(date == null || il == null) return null;
        // GET all sentences from news
        String[] cumleler = dateDeletedString(this.text).replace('\n',' ').split(Pattern.quote("."));
        int vaka =-1 ;
        int vefat=-1 ;
        int taburcu =-1 ;
        for(int a=0;a<cumleler.length;a++){
            java.lang.String cumle = cumleler[a];
            if(cumleHasWord(cumle,"vaka")){
                vaka = getIntFromCumle(cumle);
            }
            else if(cumleHasWord(cumle,"vefat")){
                vefat = getIntFromCumle(cumle);
            }
            else if(cumleHasWord(cumle,"taburcu")){
                taburcu = getIntFromCumle(cumle);
            }

        }
        // if any of those doesnt exist, its not a valid coroona news
        if(vaka==-1||vefat==-1||taburcu==-1|| date == null || il==null){
    return null;
        }else{
            //if exist return corona case
            CoronaCase c = new CoronaCase();
            c.setCity(il);
            c.setTaburcuSayisi(taburcu);
            c.setVakaSayisi(vaka);
            c.setVefatSayisi(vefat);
            c.setDate(date);
        return c;
        }
    }

    // find int in a sentence if exist
    private int getIntFromCumle(String cumle){
        String[] kelimeler =cumle.split(" ");
        for(int a=0;a<kelimeler.length;a++){

            String stringToTest = kelimeler[a];

            try {
                return Integer.parseInt(stringToTest);
            } catch (Exception e) {
                continue;
            }
        }
        return -1;
    }
    //find  il in news
    private String getIl(){
        String[] kelimeler = this.text.split(" ");
        List<String> list = Arrays.asList(this.iller);
        for(int a=0;a<kelimeler.length;a++){
            if(list.contains(kelimeler[a])){
                return kelimeler[a];
            }
        }
        return null;
    }
    // find date in news 
    private LocalDate getDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String[] kelimeler = this.text.split(" ");
        List<String> list = Arrays.asList(this.iller);
        for(int a=0;a<kelimeler.length;a++){

            String stringToTest = kelimeler[a];

            try {
                LocalDate dateTime = LocalDate .parse(stringToTest, formatter);
                return dateTime;
            } catch (DateTimeParseException dtpe) {
                continue;
            }
        }
        return null;
    }
    // determine wheteher cumle has the word
    private boolean cumleHasWord(String cumle,String word){
        String[] kelimeler = cumle.split(" ");
       return  Arrays.asList(kelimeler).contains(word);
    }
}
