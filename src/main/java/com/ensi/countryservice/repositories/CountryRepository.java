package com.ensi.countryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensi.countryservice.beans.Country;

public interface CountryRepository extends JpaRepository<Country,Integer> {

}