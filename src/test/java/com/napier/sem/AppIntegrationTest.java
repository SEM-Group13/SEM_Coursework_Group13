package com.napier.sem;

import org.junit.jupiter.api.AfterAll;
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

    @AfterAll
    static void deInit(){
        app.disconnect();
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
    void getTop_N_Cities_District() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Cities_District("Buenos Aires", n); //Test should still work if "Buenos Aires" is not in db
        assert (cities.size() <= n);
        for (int i = 0; i < n - 1; i++) {
            assert (cities.get(i).population >= cities.get(i + 1).population);
            assertEquals(cities.get(i).district, cities.get(i+1).district);
        }
        app.printCities(cities);
    }

    /**
     *Test that app.getTop_N_Cities_District() can be printed with app.printCountries
     * Check they are in the correct order
     * Check output does go not over desired limit
     */
    @Test
    void getTop_N_Countries_World() {
        int n = 3;
        ArrayList<Country> countries = app.getTop_N_Countries_World(n);

        assert (countries.size() <= n);
        for (int i = 0; i < n - 1; i++) {

            assert (countries.get(i).population >= countries.get(i + 1).population);
        }
        app.printCountries(countries);


    }

    /**
     * Checks that population is ordered correctly(using Europe as a sample but the test will still pass if "Europe" does not exist)
     * Checks if it can output
     */
    @Test
    void getTop_N_Captial_Cities_Continent() {
        int n = 3;
        ArrayList<City> cities = app.getTop_N_Capital_Cities_Continent("Europe", 3);
        app.printCities(cities);
        if(cities.size()>0){
            for(int i=0; i<n-1; i++){

                assert(cities.get(i).population>=cities.get(i+1).population);
            }
        }
    }

    @Test
    void getPopulation_World() {
double pop = app.getPopulation_World();

assert(pop>=0);
    }

    /*
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

    }*/

    @Test
    void getPopulation_Country() {
        ArrayList<Country> countries = app.getTop_N_Countries_World(1);
        Country country = app.getPopulation_Country(countries.get(0).name);

//        assert(country.population>=0);
        assertEquals(countries.get(0).population, country.population);
    }
//
//    @Test
//    void getPopulation_Country() {
//        Country country = app.getPopulation_Country("Aruba");
//        assertEquals(country.name, "Aruba");
//        assertEquals(country.population, 103000);
//    }
//
//    @Test
//    void getPopulation_World() {
//        double population = app.getPopulation_World();
//        assertEquals(population, 6.07874945E9);
//    }
//
//    @Test
//    void getPopulation_Continent() {
//        double population = app.getPopulation_Continent("Asia");
//        assertEquals(population, 3.7050257E9);
//    }
//
//    @Test
//    void getPopulation_Region() {
//        double population = app.getPopulation_Region("Central Africa");
//        assertEquals(population, 9.5652E7);
//    }
//
//    @Test
//    void getPopulation_District() {
//        double population = app.getPopulation_District("Scotland");
//        assertEquals(population, 1429620.0);
//    }
}