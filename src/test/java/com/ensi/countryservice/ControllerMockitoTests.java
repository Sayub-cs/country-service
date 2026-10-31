package com.ensi.countryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ensi.countryservice.beans.Country;
import com.ensi.countryservice.controllers.CountryController;
import com.ensi.countryservice.services.CountryService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { ControllerMockitoTests.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerMockitoTests {

	@Mock
	CountryService countryService;

	@InjectMocks
	CountryController countryController;

	public List<Country> mycountries;
	Country country;

	@Test
	@Order(1)
	public void test_getAllCountries() {
		mycountries = new ArrayList<Country>();
		mycountries.add(new Country(1, "India", "Delhi"));
		mycountries.add(new Country(2, "USA", "Washington"));

		when(countryService.getAllCountries()).thenReturn(mycountries);// Mocking
		ResponseEntity<List<Country>> res = countryController.getCountries();

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}

	@Test
	@Order(2)
	public void test_getAllCountries_empty() {
		when(countryService.getAllCountries()).thenReturn(new ArrayList<Country>());// Mocking
		ResponseEntity<List<Country>> res = countryController.getCountries();

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@Order(3)
	public void test_getCountryByID() {
		country = new Country(1, "India", "Delhi");

		int countryID = 1;
		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking
		ResponseEntity<Country> res = countryController.getCountryById(countryID);

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(1, res.getBody().getIdCountry());
	}

	@Test
	@Order(4)
	public void test_getCountryByID_notFound() {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking
		ResponseEntity<Country> res = countryController.getCountryById(99);

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@Order(5)
	public void test_getCountryByName() {
		country = new Country(1, "India", "Delhi");

		String countryName = "India";
		when(countryService.getCountryByName(countryName)).thenReturn(country);// Mocking
		ResponseEntity<Country> res = countryController.getCountryByName(countryName);

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(countryName, res.getBody().getName());
	}

	@Test
	@Order(6)
	public void test_getCountryByName_notFound() {
		when(countryService.getCountryByName("Spain")).thenReturn(null);// Mocking
		ResponseEntity<Country> res = countryController.getCountryByName("Spain");

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@Order(7)
	public void test_addCountry() {
		country = new Country(3, "Germany", "Berlin");

		when(countryService.addCountry(country)).thenReturn(country);// Mocking
		ResponseEntity<Country> res = countryController.addCountry(country);

		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(country, res.getBody());
	}

	@Test
	@Order(8)
	public void test_updateCountry() {
		country = new Country(3, "Japan", "Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking
		when(countryService.updateCountry(country)).thenReturn(country);// Mocking

		ResponseEntity<Country> res = countryController.updateCountry(countryID, country);

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals("Japan", res.getBody().getName());
		assertEquals("Tokyo", res.getBody().getCapital());
	}

	@Test
	@Order(9)
	public void test_updateCountry_notFound() {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking

		ResponseEntity<Country> res = countryController.updateCountry(99, new Country(99, "Japan", "Tokyo"));

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@Order(10)
	public void test_deleteCountry() {
		country = new Country(3, "Japan", "Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking

		ResponseEntity<Country> res = countryController.deleteCountry(countryID);

		assertEquals(HttpStatus.OK, res.getStatusCode());
	}

	@Test
	@Order(11)
	public void test_deleteCountry_notFound() {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking

		ResponseEntity<Country> res = countryController.deleteCountry(99);

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@Order(12)
	public void test_countryExists() {
		when(countryService.existsById(1)).thenReturn(true);// Mocking

		ResponseEntity<Boolean> res = countryController.countryExists(1);

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertTrue(res.getBody());
	}

	@Test
	@Order(13)
	public void test_countCountries() {
		when(countryService.countCountries()).thenReturn(2L);// Mocking

		ResponseEntity<Long> res = countryController.countCountries();

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(2L, res.getBody());
	}
}