package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {

        // Create new Application
        App a = new App();

        // Connect to database
        if (args.length < 1)
        {
            a.connect("localhost:3306");
        }
        else
        {
            a.connect(args[0]);
        }

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


    public Country getCountry_From_City(City city){
        String countryid="'" + city.country + "'";
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE code=" + countryid;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            Country country = new Country();
            while(rset.next()){


                country.code=rset.getString("code");
                country.name=rset.getString("name");
                country.continent=rset.getString("continent");
                country.region=rset.getString("region");
                country.population=rset.getInt("population");
                country.capital=rset.getInt("capital");
                countries.add(country);
            }
            return country;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<City> getCities_Continent_By_LS(String cont){
        cont="'"+cont+"'";
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, continent "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE continent="+cont
                            + " ORDER BY city.population ASC";
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
     * get Countries of a continent sorted by large to small
     * @return countries
     */
    public ArrayList<Country> getCountries_Continent_By_LS(String cont){
        cont="'"+cont+"'";
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE continent=" + cont
                            + " ORDER BY population ASC";
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

    /**
     * Gets details for one country based on their code
     * Not sure if this is required by specification but is useful for testing
     * @param countryid
     * @return
     */
    public Country getCountry_World_By_Id(String countryid){
        countryid="'" + countryid + "'";
        try {
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE code=" + countryid;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            Country country = new Country();
            while(rset.next()){


                country.code=rset.getString("code");
                country.name=rset.getString("name");
                country.continent=rset.getString("continent");
                country.region=rset.getString("region");
                country.population=rset.getInt("population");
                country.capital=rset.getInt("capital");
                countries.add(country);
            }
            return country;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }


    /**
     * Get the top n population countries in the world
     * @param n
     * @return countries
     */
    public ArrayList<Country> getTop_N_Countries_World(int n){
        try {

            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);


            ArrayList<Country> countries= new ArrayList<Country>();
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
            System.out.println("Failed to get Country details");
            return null;
        }
    }


    /**
     * Gets top N Cities in a district
     * @param dist
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Cities_District(String dist, int n){
        try {
            dist="'" + dist + "'";
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT name, countrycode, district, population "
                            + "FROM city "
                            + "WHERE district = " + dist
                            + " LIMIT " + n;
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
            System.out.println("Failed to get City details");
            return null;
        }
    }


    /**
     * Gets the top N most populated capital cities of a continent
     * @param cont
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Capital_Cities_Continent(String cont, int n){
        try {
            cont="'" + cont + "'";
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code)"
                            + "WHERE continent = " + cont
                            + " AND capital=ID"
                            +" ORDER BY city.population DESC"
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);
            //System.out.println("IGNORE ME");
            ArrayList<City> cities = new ArrayList<City>();
            while(rset.next()){
                City city = new City();
                city.name=rset.getString("city.Name");
                city.country=rset.getString("CountryCode");
                city.district=rset.getString("District");
                city.population=rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Gets the top N most populated capital cities of the world
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Capital_Cities_World( int n){
        try {

            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code)"

                            + " AND capital=ID"
                            +" ORDER BY city.population DESC"
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);
            //System.out.println("IGNORE ME");
            ArrayList<City> cities = new ArrayList<City>();
            while(rset.next()){
                City city = new City();
                city.name=rset.getString("city.Name");
                city.country=rset.getString("CountryCode");
                city.district=rset.getString("District");
                city.population=rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
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
        System.out.println(String.format("|%-20s | %-15s | %-20s | %-30s|", "City Name", "Country Code", "District", "Population" ));
        for(City city: cities){
            if(city==null) continue;

            String city_string=String.format("|%-20s | %-15s | %-20s | %-30d|", city.name, city.country, city.district, city.population);
            System.out.println(city_string);
        }
    }

}