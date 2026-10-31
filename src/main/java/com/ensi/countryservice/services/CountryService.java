package com.ensi.countryservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensi.countryservice.beans.Country;
import com.ensi.countryservice.repositories.CountryRepository;

@Service
public class CountryService {

    @Autowired
    CountryRepository countryRep;

    public List<Country> getAllCountries() {
        return countryRep.findAll();
    }

    public Country getCountryById(int id) {

        List<Country> countries = countryRep.findAll();

        for (Country con : countries) {
            if (con.getIdCountry() == id) {
                return con;
            }
        }

        return null;
    }

    public Country getCountryByName(String name) {

        List<Country> countries = countryRep.findAll();

        for (Country con : countries) {
            if (con.getName().equalsIgnoreCase(name)) {
                return con;
            }
        }

        return null;
    }

    public Country addCountry(Country country) {
        return countryRep.save(country);
    }

    public Country updateCountry(Country country) {
        return countryRep.save(country);
    }

    public void deleteCountry(Country country) {
        countryRep.delete(country);
    }

    public boolean existsById(int id) {
        return countryRep.existsById(id);
    }

    public boolean existsByName(String name) {

        List<Country> countries = countryRep.findAll();

        for (Country country : countries) {
            if (country.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public long countCountries() {
        return countryRep.count();
    }

    public void deleteCountryById(int id) {
        countryRep.deleteById(id);
    }
}