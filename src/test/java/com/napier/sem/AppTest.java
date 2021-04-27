package com.napier.sem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    /**
     * Create a new app object for use in most tests
     */
    @BeforeAll
    static void init()
    {
        app = new App();
    }



    /**
     * Attempt to use app.printCities without any meaningful input
     */
    @Test
    void printCitiesTestNull()
    {
        app.printCities(null);
    }

    /**
     * Attempts to use app.printCities with a cities arraylist with nothing in it
     */
    @Test
    void printCitiesTestEmpty(){
        ArrayList<City> cities = new ArrayList<City>();
        app.printCities(cities);
    }


    /**
     * Print cities with sample data
     */
    @Test void printCities(){
        ArrayList<City> cities = new ArrayList<City>();
        City c = new City();
        c.population=15;
        c.district="Fakeistan";
        c.country="FKE";
        c.name="Fakeopolis";
        c.city_no=0;
        cities.add(c);
        app.printCities(cities);
    }

    /**
     * Print countries with sample data
     */
    @Test void printCountries(){
        ArrayList<Country> countries = new ArrayList<Country>();
        Country c = new Country();
        c.code="FKE";
        c.name="Fakeland";
        c.continent="Big Fakeia";
        c.region="The Fake Steppe";
        c.population=16;
        c.capital=0;
        countries.add(c);
        app.printCountries(countries);
    }
}