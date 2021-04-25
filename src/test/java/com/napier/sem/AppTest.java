package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{//ignore me
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printCitiesTestNull()
    {
        app.printCities(null);
    }

    @Test
    void printCountriesTestNull(){
        app.printCountries(null);
    }

    @Test
    void printCitiesTestEmpty(){
        ArrayList<City> cities = new ArrayList<City>();
        app.printCities(cities);
    }

    @Test
    void printCountriesTestEmpty(){
        ArrayList<Country> countries = new ArrayList<Country>();
        app.printCountries(countries);
    }

    @Test void printCititesTestContainsNull(){
        ArrayList<City> cities = new ArrayList<City>();
        cities.add(null);
        app.printCities(cities);
    }

    @Test void printCountriesTestContainsNull(){
        ArrayList<Country> countries = new ArrayList<Country>();
        countries.add(null);
        app.printCountries(countries);
    }

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