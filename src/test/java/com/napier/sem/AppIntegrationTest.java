package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060");
    }

    @Test
    void getCountry_World_By_N(){

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
        int n=3;
        ArrayList<City> cities = app.getTop_N_Cities_District("Buenos Aires", n);
        ArrayList<City> test = new ArrayList<City>();
        City testcity1 = new City();
        City testcity2 = new City();
        City testcity3 = new City();

        testcity1.name="La Matanza";
        testcity1.country="ARG";
        testcity1.district="Buenos Aires";
        testcity1.population=1266461;
        test.add(testcity1);

        testcity2.name="Lomas de Zamora";
        testcity2.country="ARG";
        testcity2.district="Buenos Aires";
        testcity2.population=622013;
        test.add(testcity2);

        testcity3.name="Quilmes";
        testcity3.country="ARG";
        testcity3.district="Buenos Aires";
        testcity3.population=559249;
        test.add(testcity3);


        for (int i=0; i<n; i++) {
            assertEquals(cities.get(i).name, test.get(i).name);
            assertEquals(cities.get(i).country, test.get(i).country);
            assertEquals(cities.get(i).district, test.get(i).district);
            assertEquals(cities.get(i).population, test.get(i).population);
//            assertEquals(cities.get(i).name, test.get(i).name);

        }
        //app.printCities(cities);
    }
}