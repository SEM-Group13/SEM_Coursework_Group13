package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    static App app;

    /**
     * Create new app for use in tests and connect to docker database
     */
    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060");
    }

//    @Test
//    void getCountry_World_By_N() {
//
//        Country country = app.getCountry_World_By_Id("CHE");
//        assertEquals(country.code, "CHE");
//        assertEquals(country.name, "Switzerland");
//        assertEquals(country.continent, "Europe");
//        assertEquals(country.region, "Western Europe");
//        assertEquals(country.population, 7160400);
//        assertEquals(country.capital, 3248);
//    }

    /**
     * Test that app.getTop_N_Cities_District() can be printed with app.printCities
     * Check that cities are in the correct order
     * Check that the output does not return more than the specified limit
     */
    @Test
    void getTop_N_Cities_World() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Cities_District("Buenos Aires", n); //Test should still work if "Buenos Aires" is not in db
        assert (cities.size() <= n);
        for (int i = 0; i < n-1; i++) {
            assert(cities.get(i).population>=cities.get(i+1).population);
        }
        app.printCities(cities);
    }

    /**
     *
     */
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

        assertEquals(cities.get(0).name, "Moscow");
        assertEquals(cities.get(1).name, "London");
        assertEquals(cities.get(2).name, "Berlin");
    }

    @Test
    void getTop_N_Captial_Cities_World() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Capital_Cities_World(3);
        app.printCities(cities);

        assertEquals(cities.get(0).name, "Seoul");
        assertEquals(cities.get(1).name, "Jakarta");
        assertEquals(cities.get(2).name, "Ciudad de México");
    }

    @Test
    void getCountries_Continent_By_LS() {
        ArrayList<Country> countries = app.getCountries_Continent_By_LS("Europe");
        app.printCountries(countries);

        for (int i = 0; i < countries.size(); i++) {
            assertEquals(countries.get(i).continent, "Europe");
            if (i > 0) {
                assertTrue(countries.get(i).population >= countries.get(i - 1).population);

            }
        }

    }

    @Test
    void getCities_Continent_By_LS() {
        ArrayList<City> cities = app.getCities_Continent_By_LS("Europe");
        app.printCities(cities);
        Country country = new Country();
        for (int i = 0; i < cities.size(); i++) {
            //assertEquals(cities.get(i).continent, "Europe");
            country = app.getCountry_From_City(cities.get(i));
            assertEquals(country.continent, "Europe");
            if (i > 0) {
                assertTrue(cities.get(i).population >= cities.get(i - 1).population);

            }


        }

    }

    @Test
    void getCountries_Region_By_LS() {
        ArrayList<Country> countries = app.getCountries_Region_By_LS("Western Europe");
        app.printCountries(countries);

        for (int i = 0; i < countries.size(); i++) {
            assertEquals(countries.get(i).region, "Western Europe");
            if (i > 0) {
                assertTrue(countries.get(i).population >= countries.get(i - 1).population);

            }
        }

    }

    @Test
    void getPopulation_City() {
        City city = app.getPopulation_City("Edinburgh");
        assertEquals(city.name, "Edinburgh");
        assertEquals(city.population, 450180);
    }

    @Test
    void getPopulation_Country() {
        Country country = app.getPopulation_Country("Aruba");
        assertEquals(country.name, "Aruba");
        assertEquals(country.population, 103000);
    }

    @Test
    void getPopulation_World() {
        double population = app.getPopulation_World();
        assertEquals(population, 6.07874945E9);
    }

    @Test
    void getPopulation_Continent() {
        double population = app.getPopulation_Continent("Asia");
        assertEquals(population, 3.7050257E9);
    }

    @Test
    void getPopulation_Region() {
        double population = app.getPopulation_Region("Central Africa");
        assertEquals(population, 9.5652E7);
    }

    @Test
    void getPopulation_District() {
        double population = app.getPopulation_District("Scotland");
        assertEquals(population, 1429620.0);
    }
}