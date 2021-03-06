package com.napier.sem;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class App {
    public static void main(String[] args) {

        // Create new Application
        App a = new App();

        // Connect to database
        if (args.length < 1) {
            a.connect("localhost:33060");
        } else {
            a.connect(args[0]);
        }
        int n =4;
        ArrayList<Country> countries = new ArrayList<Country>();


        System.out.println("example 1");
        countries= a.getCountries_World_By_LS();
        printCountries(countries);

        System.out.println("example 2");
        countries=a.getCountries_Continent_By_LS("Europe");
        printCountries(countries);

        System.out.println("example 3");
        countries=a.getCountries_Region_By_LS("Western Europe");
        printCountries(countries);

        System.out.println("example 4");
        countries=a.getTop_N_Countries_World(n);
        printCountries(countries);

        System.out.println("example 5");
        countries=a.get_Top_N_Countries_Continent("Europe", n);
        printCountries(countries);

        System.out.println("example 6");
        countries=a.getTop_N_Countries_Region("Western Europe", n);
        printCountries(countries);

        ArrayList<City> cities = new ArrayList<City>();
        System.out.println("example 7");
        cities=a.getCities_World_By_LS();
        printCities(cities);

        System.out.println("example 8");
        cities=a.getCities_Continent_By_LS("Europe");
        printCities(cities);

        System.out.println("example 9");
        cities=a.getCities_Region_By_LS("Western Europe");
        printCities(cities);

        System.out.println("example 10");
        cities=a.getCities_Country_By_LS("France");
        printCities(cities);

        System.out.println("example 11");
        cities=a.getCities_District_By_LS("Île-de-France");
        printCities(cities);

        System.out.println("example 12");
        cities=a.getTop_N_Cities_World(n);
        printCities(cities);

        System.out.println("example 13");
        cities=a.getTop_N_Cities_Continent("Europe", n);
        printCities(cities);

        System.out.println("example 14");
        cities=a.getTop_N_Cities_Region("Western Europe", n);
        printCities(cities);

        System.out.println("example 15");
        cities=a.getTop_N_Cities_Country("France", n);
        printCities(cities);

        System.out.println("example 16");
        cities=a.getTop_N_Cities_District("Île-de-France", n);
        printCities(cities);

        System.out.println("example 17");
        cities=a.getCapital_Cities_World_By_LS();
        printCities(cities);

        System.out.println("example 18");
        cities=a.getCapital_Cities_Continent_By_LS("Europe");
        printCities(cities);

        System.out.println("example 19");
        cities=a.getCapital_Cities_Region_By_LS("Western Europe");
        printCities(cities);

        System.out.println("example 20");
        cities=a.getTop_N_Capital_Cities_World(n);
        printCities(cities);

        System.out.println("example 21");
        cities=a.getTop_N_Capital_Cities_Continent("Europe", n);
        printCities(cities);

        System.out.println("example 22");
        cities=a.getTop_N_Capital_Cities_Region("Western Europe", n);
        printCities(cities);

        Population population= new Population();
        ArrayList<Population> populations = new ArrayList<Population>();
        System.out.println("example 23");
        population=a.get_Urban_Rural_Percentage_Continent("Europe");
        populations.add(population);
        printPopulations(populations);
        populations.clear();

        System.out.println("example 24");
        population=a.get_Urban_Rural_Percentage_Region("Western Europe");
        populations.add(population);
        printPopulations(populations);
        populations.clear();

        System.out.println("example 25");
        population=a.get_Urban_Rural_Percentage_Continent("France");
        populations.add(population);
        printPopulations(populations);
        populations.clear();

        System.out.println("example 26");
        System.out.println(a.getPopulation_World());

        System.out.println("example 27");
        System.out.println(a.getPopulation_Continent("Europe"));

        System.out.println("example 28");
        System.out.println(a.getPopulation_Region("Western Europe"));

        System.out.println("example 29");
        System.out.println(a.getPopulation_Country("France"));

        System.out.println("example 30");
        System.out.println(a.getPopulation_District("Île-de-France"));

        System.out.println("example 31");
        System.out.println(a.getPopulation_City("Paris"));

        System.out.println("example 32");
        String[] langs = {"Chinese", "English", "Hindi", "Spanish", "Arabic"};
        a.languageReport(langs);

        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
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
     *
     * @return countries
     */
    public ArrayList<Country> getCountries_World_By_LS() {
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();

                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets the cities of the world sorted by larges pop to smallest
     *
     * @return cities
     */
    public ArrayList<City> getCities_World_By_LS() {
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT name, countrycode, district, population "
                            + "FROM city "
                            + "ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets population in and out of cities + percentages and total
     *
     * @param cont
     * @return population
     */
    public Population get_Urban_Rural_Percentage_Continent(String cont) {
        cont = "'" + cont + "'";
        try {
            Statement stmt = con.createStatement();

            String select = "SELECT country.continent, SUM(country.population), " +
                    " (SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.continent=" + cont + ")" +
                    " FROM country WHERE country.continent=" + cont;
            ResultSet rset = stmt.executeQuery(select);
            ArrayList<Population> populations = new ArrayList<Population>();
            Population population = new Population();
            while (rset.next()) {
                population.place = rset.getString("country.continent");
                population.total = rset.getInt("SUM(country.population)");
                population.urban = rset.getInt("(SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.continent=" + cont + ")");
                population.rural = population.total - population.urban;
                population.pUrban = (Double.valueOf(population.urban) / Double.valueOf(population.total)) * 100;
                population.pRural = (Double.valueOf(population.rural) / Double.valueOf(population.total)) * 100;
                populations.add(population);
            }
            return population;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets population in and out of cities + percentages and total
     *
     * @param region
     * @return population
     */
    public Population get_Urban_Rural_Percentage_Region(String region) {
        region = "'" + region + "'";
        try {
            Statement stmt = con.createStatement();

            String select = "SELECT country.region, SUM(country.population), " +
                    " (SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.region=" + region + ")" +
                    " FROM country WHERE country.region=" + region;
            ResultSet rset = stmt.executeQuery(select);
            ArrayList<Population> populations = new ArrayList<Population>();
            Population population = new Population();
            while (rset.next()) {
                population.place = rset.getString("country.region");
                population.total = rset.getInt("SUM(country.population)");
                population.urban = rset.getInt("(SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.region=" + region + ")");
                population.rural = population.total - population.urban;
                population.pUrban = (Double.valueOf(population.urban) / Double.valueOf(population.total)) * 100;
                population.pRural = (Double.valueOf(population.rural) / Double.valueOf(population.total)) * 100;
                populations.add(population);
            }
            return population;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets population in and out of cities + percentages and total
     *
     * @param country
     * @return population
     */
    public Population get_Urban_Rural_Percentage_Country(String country) {
        country = "'" + country + "'";
        try {
            Statement stmt = con.createStatement();

            String select = "SELECT country.name, SUM(country.population), " +
                    " (SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.name=" + country + ")" +
                    " FROM country WHERE country.name=" + country;
            ResultSet rset = stmt.executeQuery(select);
            ArrayList<Population> populations = new ArrayList<Population>();
            Population population = new Population();
            while (rset.next()) {
                population.place = rset.getString("country.name");
                population.total = rset.getInt("SUM(country.population)");
                population.urban = rset.getInt("(SELECT SUM(city.population) FROM city JOIN country ON (city.countrycode=country.code) WHERE country.name=" + country + ")");
                population.rural = population.total - population.urban;
                population.pUrban = (Double.valueOf(population.urban) / Double.valueOf(population.total)) * 100;
                population.pRural = (Double.valueOf(population.rural) / Double.valueOf(population.total)) * 100;
                populations.add(population);
            }
            return population;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }


    /**
     * Get the country that a city is in
     *
     * @param city
     * @return country
     */
    public Country getCountry_From_City(City city) {
        String countryid = "'" + city.country + "'";
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
            while (rset.next()) {


                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return country;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }


    /**
     * get the cities in a continent by large pop to small
     *
     * @param cont
     * @return cities
     */
    public ArrayList<City> getCities_Continent_By_LS(String cont) {
        cont = "'" + cont + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, district, city.population, continent "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE continent=" + cont
                            + " ORDER BY city.population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * get Countries of a continent sorted by large to small
     *
     * @return countries
     */
    public ArrayList<Country> getCountries_Continent_By_LS(String cont) {
        cont = "'" + cont + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE continent=" + cont
                            + " ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();

                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get Countries by Region, ordered by Population
     *
     * @param region
     * @return
     */
    public ArrayList<Country> getCountries_Region_By_LS(String region) {
        region = "'" + region + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE region=" + region
                            + " ORDER BY population ASC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();

                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets details for one country based on their code
     * Not sure if this is required by specification but is useful for testing
     *
     * @param countryid
     * @return
     */
    public Country getCountry_World_By_Id(String countryid) {
        countryid = "'" + countryid + "'";
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
            while (rset.next()) {


                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return country;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get top N populated Countries in a Continent
     *
     * @param continent
     * @param n
     * @return
     */
    public ArrayList<Country> getTop_N_Countries_Continent(String continent, int n) {
        try {
            continent = "'" + continent + "'";

            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE continent=" + continent
                            + " ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();
                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Country details");
            return null;
        }
    }

    /**
     * Get top N populated Countries in a Region
     *
     * @param region
     * @param n
     * @return
     */
    public ArrayList<Country> getTop_N_Countries_Region(String region, int n) {
        try {

            region = "'" + region + "'";
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE region=" + region
                            + "ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();
                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Country details");
            return null;
        }
    }

    /**
     * Get the top n population countries in the world
     *
     * @param n
     * @return countries
     */
    public ArrayList<Country> getTop_N_Countries_World(int n) {
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


            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();
                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Country details");
            return null;
        }
    }


    /**
     * Gets top N Cities in a district
     *
     * @param dist
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Cities_District(String dist, int n) {
        try {
            dist = "'" + dist + "'";
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
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }


    /**
     * Gets the top N most populated capital cities of a continent
     *
     * @param cont
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Capital_Cities_Continent(String cont, int n) {
        try {
            cont = "'" + cont + "'";
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code)"
                            + "WHERE continent = " + cont
                            + " AND capital=ID"
                            + " ORDER BY city.population DESC"
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);
            //System.out.println("IGNORE ME");
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Gets the top N most populated capital cities of the world
     *
     * @param n
     * @return cities
     */
    public ArrayList<City> getTop_N_Capital_Cities_World(int n) {
        try {

            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code)"

                            + " AND capital=ID"
                            + " ORDER BY city.population DESC"
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);
            //System.out.println("IGNORE ME");
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Get top N Cities in a Continent
     *
     * @param continent
     * @param n
     * @return
     */
    public ArrayList<City> getTop_N_Cities_Continent(String continent, int n) {
        continent = "'" + continent + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, city.countrycode, country.continent, city.population "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE country.continent= " + continent
                            + " ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country = rset.getString("CountryCode");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get top N Cities in the World
     *
     * @param n
     * @return
     */
    public ArrayList<City> getTop_N_Cities_World(int n) {
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT name, countrycode, population "
                            + "FROM city "
                            + "ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Get top N Cities in a Country
     *
     * @param country
     * @param n
     * @return
     */
    public ArrayList<City> getTop_N_Cities_Country(String country, int n) {
        country = "'" + country + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, city.countrycode, country.name, city.population "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE country.name= " + country
                            + " ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country = rset.getString("CountryCode");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }


    /**
     * Gets the top N countries in a continent
     *
     * @param cont
     * @param n
     * @return countries
     */
    public ArrayList<Country> get_Top_N_Countries_Continent(String cont, int n) {
        cont = "'" + cont + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE continent=" + cont
                            + " ORDER BY population DESC "
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();

                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets top N countries in a region
     *
     * @param reg
     * @param n
     * @return countries
     */
    public ArrayList<Country> get_Top_N_Countries_Region(String reg, int n) {
        reg = "'" + reg + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT code, name, continent, region, population, capital "
                            + "FROM country "
                            + "WHERE region=" + reg
                            + " ORDER BY population DESC "
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country country = new Country();

                country.code = rset.getString("code");
                country.name = rset.getString("name");
                country.continent = rset.getString("continent");
                country.region = rset.getString("region");
                country.population = rset.getInt("population");
                country.capital = rset.getInt("capital");
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets cities of a region large to small
     *
     * @param reg
     * @return cities
     */
    public ArrayList<City> getCities_Region_By_LS(String reg) {
        reg = "'" + reg + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, district, city.population, continent, region "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE region=" + reg
                            + " ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get all Capital Cities in the World, large to small
     *
     * @return
     */
    public ArrayList<City> getCapital_Cities_World_By_LS() {
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, city.district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE capital=ID "
                            + "ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Gets cities of a country large to small
     *
     * @param cont
     * @return cities
     */
    public ArrayList<City> getCities_Country_By_LS(String cont) {
        cont = "'" + cont + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, district, city.population, continent, country.name "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE country.name=" + cont
                            + " ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get all Capital Cities in a Continent, large to small
     *
     * @param continent
     * @return
     */
    public ArrayList<City> getCapital_Cities_Continent_By_LS(String continent) {
        try {
            continent = "'" + continent + "'";

            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, city.district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE continent = " + continent
                            + " AND capital=ID "
                            + "ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Gets cities of a district large to small
     *
     * @param dist
     * @return cities
     */
    public ArrayList<City> getCities_District_By_LS(String dist) {
        dist = "'" + dist + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, district, city.population, continent, country.name "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE district=" + dist
                            + " ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets top N populated capital Cities in a Region, large to small
     *
     * @param region
     * @param n
     * @return
     */
    public ArrayList<City> getTop_N_Capital_Cities_Region(String region, int n) {
        try {
            region = "'" + region + "'";
            //Create SQL statment
            Statement stmt = con.createStatement();

            //Make the SQL string iteslf
            String select =
                    "SELECT city.name, countrycode, district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code)"
                            + "WHERE region= " + region
                            + " AND capital=ID"
                            + " ORDER BY city.population DESC"
                            + " LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Get all Capital Cities in a Region, large to small
     *
     * @param region
     * @return
     */
    public ArrayList<City> getCapital_Cities_Region_By_LS(String region) {
        try {
            region = "'" + region + "'";

            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, countrycode, city.district, city.population, code "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE region = " + region
                            + " AND capital=ID "
                            + "ORDER BY city.population DESC";
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.Name");
                city.country = rset.getString("CountryCode");
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get City details");
            return null;
        }
    }

    /**
     * Get top N Cities in a Region
     *
     * @param region
     * @param n
     * @return
     */
    public ArrayList<City> getTop_N_Cities_Region(String region, int n) {
        region = "'" + region + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Make the SQL string itself
            String select =
                    "SELECT city.name, city.countrycode, country.region, city.population "
                            + "FROM city "
                            + "JOIN country ON (countrycode=code) "
                            + "WHERE country.region= " + region
                            + " ORDER BY population DESC "
                            + "LIMIT " + n;
            ResultSet rset = stmt.executeQuery(select);

            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country = rset.getString("CountryCode");
                city.population = rset.getInt("Population");
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Outputs report of countries table
     *
     * @param countries
     */
    public static void printCountries(ArrayList<Country> countries) {
        //Insert header here
        if (countries == null) {
            System.out.println(("No Countries"));
            return;
        }
        System.out.println(String.format("|%-15s | %-30s | %-15s | %-40s | %-15s | %-15s|", "Country Code", "Country Name", "Continent", "Region", "Population", "Capital"));
        for (Country country : countries) {
            if (country == null) continue;
            String coun_string = String.format("|%-15s | %-30s | %-15s | %-40s | %-15d | %-15d|", country.code, country.name, country.continent, country.region, country.population, country.capital);
            System.out.println(coun_string);
        }
    }

    /**
     * Outputs report of cities table
     *
     * @param cities
     */
    public static void printCities(ArrayList<City> cities) {
        if (cities == null) {
            System.out.println(("No Cities"));
            return;
        }
        //Insert header here
        System.out.println(String.format("|%-20s | %-15s | %-20s | %-30s|", "City Name", "Country Code", "District", "Population"));
        for (City city : cities) {
            if (city == null) continue;

            String city_string = String.format("|%-20s | %-15s | %-20s | %-30d|", city.name, city.country, city.district, city.population);
            System.out.println(city_string);
        }
    }

    /**
     * prints population report
     *
     * @param populations
     */
    public static void printPopulations(ArrayList<Population> populations) {
        if (populations == null) {
            System.out.println("No Populations");
            return;
        }
        System.out.println(String.format("|%-20s | %-10s | %-25s | %-10s | %-25s | %-10s |", "Place", "Total Pop", "% Urban", "Total Urban", "% Rural", "Total Rural"));
        for (Population population : populations) {
            if (population == null) continue;
            String pop_string = String.format("|%-20s | %-10s | %-25s | %-10s | %-25s | %-10s |", population.place, population.total, population.pUrban, population.urban, population.pRural, population.rural);
            System.out.println(pop_string);
        }
    }

    /**
     * Get the population of a city
     *
     * @return city
     */
    public City getPopulation_City(String city_name) {
        city_name = "'" + city_name + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();

            //Define SQL statement
            String select =
                    "SELECT  name, population "
                            + "FROM city "
                            + "WHERE name = " + city_name;

            // Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            // Store data as type City
            City city = new City();
            while (rset.next()) {
                city.name = rset.getString("name");
                city.population = rset.getInt("population");
            }
            return city;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Get the population of a country
     *
     * @return country
     */
    public Country getPopulation_Country(String country_name) {
        country_name = "'" + country_name + "'";
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();
            //Define SQL statement
            String select =
                    "SELECT  name, population "
                            + "FROM country "
                            + "WHERE name = " + country_name;

            // Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            // Store data as type Country
            Country country = new Country();
            while (rset.next()) {
                country.name = rset.getString("name");
                country.population = rset.getInt("population");
            }
            return country;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get the sum population of each country
     *
     * @return sum
     */
    public double getPopulation_World() {
        double sum = 0;
        try {
            //Create SQL statement
            Statement stmt = con.createStatement();
            //Define SQL statement
            String select =
                    "SELECT SUM(population) "
                            + "AS population "
                            + "FROM country";

            // Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            // Store data as type Country

            while (rset.next()) {
                sum = rset.getDouble("population");
            }
            return sum;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return 0;
        }
    }


    public void languageReport(String[] langs) {
            for (int i = 0; i < langs.length; i++) {
            Language language = new Language();
            language.totalpop = howManyPeopleSpeak(langs[i]);
            language.language = langs[i];
            System.out.println(language.language + " " + language.totalpop + " " + language.totalpop / getPopulation_World() * 100 + "%");
        }
    }

    public int howManyPeopleSpeak(String lang) {
        lang = "'" + lang + "'";
        int speakers = 0;
        int totalpop = 0;
        double percentage;
        try {
            Statement stmt = con.createStatement();

            String select =
                    "SELECT percentage, country.population, countrylanguage.countrycode " +
                            " FROM countrylanguage" +
                            " JOIN country ON (countrylanguage.countrycode=country.code)" +
                            " WHERE language=" + lang;
            ResultSet rset = stmt.executeQuery(select);


            while (rset.next()) {
                Language language = new Language();

                language.pop = rset.getInt("country.population");
                language.percentage = rset.getInt("percentage");
                speakers += Double.valueOf(language.pop) * Double.valueOf(language.percentage) / 100;

            }

            return speakers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get language details");
            return 0;
        }
    }

    /**
     * Get the population of a continent
     *
     * @return sum
     */
    public double getPopulation_Continent(String cont_name) {
        double sum = 0;
        cont_name = "'" + cont_name + "'";
        try {
            //Create SQL Statement
            Statement stmt = con.createStatement();
            //Define SQL statement
            String select =
                    "SELECT SUM(population) "
                            + "AS population "
                            + "FROM country "
                            + "WHERE continent = " + cont_name;

            //Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            //Store result as type Double
            while (rset.next()) {
                sum = rset.getDouble("population");
            }
            return sum;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get continent details");
            return 0;
        }
    }

    /**
     * Get the population of a region
     *
     * @return sum
     */
    public double getPopulation_Region(String region_name) {
        double sum = 0;
        region_name = "'" + region_name + "'";
        try {
            //Create SQL Statement
            Statement stmt = con.createStatement();
            //Define SQL statement
            String select =
                    "SELECT SUM(population) "
                            + "AS population "
                            + "FROM country "
                            + "WHERE region = " + region_name;

            //Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            //Store result as type Double
            while (rset.next()) {
                sum = rset.getDouble("population");
            }
            return sum;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get region details");
            return 0;
        }
    }

    /**
     * Get the population of a district
     *
     * @return sum
     */
    public double getPopulation_District(String district_name) {
        double sum = 0;
        district_name = "'" + district_name + "'";
        try {
            //Create SQL Statement
            Statement stmt = con.createStatement();
            //Define SQL statement
            String select =
                    "SELECT SUM(population) "
                            + "AS population "
                            + "FROM city "
                            + "WHERE district = " + district_name;

            //Return SQL result
            ResultSet rset = stmt.executeQuery(select);

            //Store result as type Double
            while (rset.next()) {
                sum = rset.getDouble("population");
            }
            return sum;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get district details");
            return 0;
        }
    }
}