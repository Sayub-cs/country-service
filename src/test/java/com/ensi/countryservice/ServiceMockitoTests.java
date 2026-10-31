package com.ensi.countryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ensi.countryservice.beans.Country;
import com.ensi.countryservice.repositories.CountryRepository;
import com.ensi.countryservice.services.CountryService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { ServiceMockitoTests.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceMockitoTests {

	@Mock
	CountryRepository countryrep;

	@InjectMocks
	CountryService countryService;

	public List<Country> mycountries;

	private List<Country> buildCountries() {
		List<Country> list = new ArrayList<Country>();
		list.add(new Country(1, "India", "Delhi"));
		list.add(new Country(2, "USA", "Washington"));
		return list;
	}

	@Test
	@Order(1)
	public void test_getAllCountries() {
		mycountries = buildCountries();

		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertEquals(2, countryService.getAllCountries().size());
	}

	@Test
	@Order(2)
	public void test_getCountryByID() {
		mycountries = buildCountries();

		int countryID = 1;
		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertEquals(countryID, countryService.getCountryById(countryID).getIdCountry());
	}

	@Test
	@Order(3)
	public void test_getCountryByID_notFound() {
		mycountries = buildCountries();

		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertNull(countryService.getCountryById(99));
	}

	@Test
	@Order(4)
	public void test_getCountryByName() {
		mycountries = buildCountries();

		String countryName = "USA";
		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertEquals(countryName, countryService.getCountryByName(countryName).getName());
	}

	@Test
	@Order(5)
	public void test_getCountryByName_notFound() {
		mycountries = buildCountries();

		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertNull(countryService.getCountryByName("Spain"));
	}

	@Test
	@Order(6)
	public void test_addCountry() {
		Country country = new Country(3, "France", "Paris");
		when(countryrep.save(country)).thenReturn(country);// Mocking
		assertEquals(country, countryService.addCountry(country));
	}

	@Test
	@Order(7)
	public void test_updateCountry() {
		Country country = new Country(3, "Germany", "Berlin");
		when(countryrep.save(country)).thenReturn(country);// Mocking
		assertEquals(country, countryService.updateCountry(country));
	}

	@Test
	@Order(8)
	public void test_deleteCountry() {
		Country country = new Country(3, "Germany", "Berlin");
		countryService.deleteCountry(country);
		verify(countryrep, times(1)).delete(country);
	}

	@Test
	@Order(9)
	public void test_existsById() {
		when(countryrep.existsById(1)).thenReturn(true);// Mocking
		when(countryrep.existsById(99)).thenReturn(false);// Mocking

		assertTrue(countryService.existsById(1));
		assertFalse(countryService.existsById(99));
	}

	@Test
	@Order(10)
	public void test_existsByName() {
		mycountries = buildCountries();

		when(countryrep.findAll()).thenReturn(mycountries);// Mocking
		assertTrue(countryService.existsByName("india"));
		assertFalse(countryService.existsByName("Spain"));
	}

	@Test
	@Order(11)
	public void test_countCountries() {
		when(countryrep.count()).thenReturn(2L);// Mocking
		assertEquals(2L, countryService.countCountries());
	}

	@Test
	@Order(12)
	public void test_deleteCountryById() {
		countryService.deleteCountryById(1);
		verify(countryrep, times(1)).deleteById(1);
	}
}