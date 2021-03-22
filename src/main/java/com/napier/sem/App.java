package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        // Create new Application
        App a = new App();

        // Connect to database
        a.connect("localhost:33060");
        ArrayList<Country> Coun= a.getCountries_World_By_LS();
        printCountries(Coun);

        ArrayList<City> City=a.getCities_World_By_LS();
        printCities(City);

        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * get Countries of the world sorted by large to small
     * @return countries
     */
    public ArrayList<Country> getCountries_World_By_LS(){
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                            "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while(rset.next()){
                Country country = new Country();

                country.code=rset.getString("code");
                country.name=rset.getString("name");
                country.continent=rset.getString("continent");
                country.region=rset.getString("region");
                country.population=rset.getInt("population");
                country.capital=rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<City> getCities_World_By_LS(){
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT name, countrycode, district, population "
                            + "FROM city "
                            + "ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while(rset.next()){
                City city = new City();
                city.name=rset.getString("Name");
                city.country=rset.getString("CountryCode");
                city.district=rset.getString("District");
                city.population=rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Outputs report of countries table
     * @param countries
     */
    public static void printCountries(ArrayList<Country> countries){
        //Insert header here
        if(countries==null){
            System.out.println(("No Countries"));
            return;
        }
        System.out.println(String.format("|%-15s | %-30s | %-15s | %-40s | %-15s | %-15s|", "Country Code","Country Name", "Continent", "Region" , "Population", "Capital"));
        for(Country country: countries){
            if(country==null) continue;
            String coun_string=String.format("|%-15s | %-30s | %-15s | %-40s | %-15d | %-15d|", country.code, country.name, country.continent, country.region, country.population, country.capital);
            System.out.println(coun_string);
        }
    }

    /**
     * Outputs report of cities table
     * @param cities
     */
    public static void printCities(ArrayList<City> cities){
        if(cities==null){
            System.out.println(("No Cities"));
            return;
        }
        //Insert header here
        System.out.println(String.format("|%-15s | %-10s | %-20s | %-30s|", "City Name", "Country Code", "District", "Population" ));
        for(City city: cities){
            if(city==null) continue;

            String city_string=String.format("|%-20s | %-10s | %-20s | %-10d|", city.name, city.country, city.district, city.population);
            System.out.println(city_string);
        }
    }

}