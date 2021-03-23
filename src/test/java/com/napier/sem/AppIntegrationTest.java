package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060");
    }

    @Test
    void getCountry_World_By_N() {

        Country country = app.getCountry_World_By_Id("CHE");
        assertEquals(country.code, "CHE");
        assertEquals(country.name, "Switzerland");
        assertEquals(country.continent, "Europe");
        assertEquals(country.region, "Western Europe");
        assertEquals(country.population, 7160400);
        assertEquals(country.capital, 3248);
    }

    @Test
    void getTop_N_Cities_World() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Cities_District("Buenos Aires", n);
        ArrayList<City> test = new ArrayList<City>();
        City testcity1 = new City();
        City testcity2 = new City();
        City testcity3 = new City();

        testcity1.name = "La Matanza";
        testcity1.country = "ARG";
        testcity1.district = "Buenos Aires";
        testcity1.population = 1266461;
        test.add(testcity1);

        testcity2.name = "Lomas de Zamora";
        testcity2.country = "ARG";
        testcity2.district = "Buenos Aires";
        testcity2.population = 622013;
        test.add(testcity2);

        testcity3.name = "Quilmes";
        testcity3.country = "ARG";
        testcity3.district = "Buenos Aires";
        testcity3.population = 559249;
        test.add(testcity3);


        for (int i = 0; i < n; i++) {
            assertEquals(cities.get(i).name, test.get(i).name);
            assertEquals(cities.get(i).country, test.get(i).country);
            assertEquals(cities.get(i).district, test.get(i).district);
            assertEquals(cities.get(i).population, test.get(i).population);
//            assertEquals(cities.get(i).name, test.get(i).name);

        }
        //app.printCities(cities);
    }

    @Test
    void getTop_N_Countries_World() {
        int n = 3;
        ArrayList<Country> countries = app.getTop_N_Countries_World(n);
        ArrayList<Country> test = new ArrayList<Country>();

        Country testcountry1 = new Country();
        Country testcountry2 = new Country();
        Country testcountry3 = new Country();
        testcountry1.code = "CHN";
        testcountry2.code = "IND";
        testcountry3.code = "USA";

        test.add(testcountry1);
        test.add(testcountry2);
        test.add(testcountry3);
        //System.out.println(countries);
//        app.printCountries(countries);

        for (int i = 0; i < n; i++) {
            assertEquals(test.get(i).code, countries.get(i).code);
        }

    }

    @Test
    void getTop_N_Captial_Cities_Continent() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Capital_Cities_Continent("Europe", 3);
        app.printCities(cities);

        assertEquals(cities.get(0).name , "Moscow");
        assertEquals(cities.get(1).name , "London");
        assertEquals(cities.get(2).name , "Berlin");
    }

    @Test
    void getTop_N_Captial_Cities_World() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Capital_Cities_World(3);
        app.printCities(cities);

        assertEquals(cities.get(0).name ,"Seoul");
        assertEquals(cities.get(1).name , "Jakarta");
        assertEquals(cities.get(2).name , "Ciudad de México");
    }

    @Test
    void getCountries_Continent_By_LS(){
        ArrayList<Country> countries = app.getCountries_Continent_By_LS("Europe");
        app.printCountries(countries);

        for (int i =0; i<countries.size();i++){
            assertEquals(countries.get(i).continent, "Europe");
            if(i>0){
                assertTrue(countries.get(i).population>countries.get(i-1).population);

            }
        }

    }
}
