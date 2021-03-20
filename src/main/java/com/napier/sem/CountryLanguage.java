package com.napier.sem;

public class CountryLanguage {
    /**
     * 3 digit ID code for country
     */
    public String country;

    /**
     * Spoken Language
     */
    public String language;

    /**
     * Is the language official
     * Data will be stored as char to accurately reflect data
     * in the database
     */
    public char is_official_char;

    /**
     * Percentage of speakers in country
     */
    public double percentage;
}
