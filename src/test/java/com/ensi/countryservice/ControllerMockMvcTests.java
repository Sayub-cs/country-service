package com.ensi.countryservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ensi.countryservice.beans.Country;
import com.ensi.countryservice.controllers.CountryController;
import com.ensi.countryservice.services.CountryService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { ControllerMockMvcTests.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.countryservice.demo")
@AutoConfigureMockMvc
@ContextConfiguration
//@WebMvcTest(ControllerMockMvcTests.class)
public class ControllerMockMvcTests {

	@Autowired
	MockMvc mockMvc;

	@Mock
	CountryService countryService;

	@InjectMocks
	CountryController countryController;

	public List<Country> mycountries;
	Country country;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
	}

	@Test
	@Order(1)
	public void test_getAllCountries() throws Exception {
		mycountries = new ArrayList<Country>();
		mycountries.add(new Country(1, "India", "Delhi"));
		mycountries.add(new Country(2, "USA", "Washington"));

		when(countryService.getAllCountries()).thenReturn(mycountries);// Mocking

		this.mockMvc.perform(get("/getcountries"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@Order(2)
	public void test_getAllCountries_empty() throws Exception {
		when(countryService.getAllCountries()).thenReturn(new ArrayList<Country>());// Mocking

		this.mockMvc.perform(get("/getcountries"))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@Order(3)
	public void test_getCountryByID() throws Exception {
		country = new Country(1, "India", "Delhi");

		int countryID = 1;
		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking

		this.mockMvc.perform(get("/getcountries/{id}", countryID))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.idCountry").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("India"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.capital").value("Delhi"))
				.andDo(print());
	}

	@Test
	@Order(4)
	public void test_getCountryByID_notFound() throws Exception {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking

		this.mockMvc.perform(get("/getcountries/{id}", 99))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@Order(5)
	public void test_getCountryByName() throws Exception {
		country = new Country(1, "India", "Delhi");

		String countryName = "India";
		when(countryService.getCountryByName(countryName)).thenReturn(country);// Mocking

		this.mockMvc.perform(get("/getcountries/countryname").param("name", "India"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.idCountry").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("India"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.capital").value("Delhi"))
				.andDo(print());
	}

	@Test
	@Order(6)
	public void test_getCountryByName_notFound() throws Exception {
		when(countryService.getCountryByName("Spain")).thenReturn(null);// Mocking

		this.mockMvc.perform(get("/getcountries/countryname").param("name", "Spain"))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@Order(7)
	public void test_addCountry() throws Exception {
		// The controller receives a new Country rebuilt from the JSON, so any() is used
		when(countryService.addCountry(any(Country.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));// Mocking

		String jsonbody = "{\"idCountry\":3,\"name\":\"Germany\",\"capital\":\"Berlin\"}";

		this.mockMvc.perform(post("/addcountry")
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Germany"))
				.andDo(print());
	}

	@Test
	@Order(8)
	public void test_updateCountry() throws Exception {
		country = new Country(3, "Germany", "Berlin");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking
		when(countryService.updateCountry(country)).thenReturn(country);// Mocking

		String jsonbody = "{\"idCountry\":3,\"name\":\"Japan\",\"capital\":\"Tokyo\"}";

		this.mockMvc.perform(put("/updatecountry/{id}", countryID)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Japan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.capital").value("Tokyo"))
				.andDo(print());
	}

	@Test
	@Order(9)
	public void test_updateCountry_notFound() throws Exception {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking

		String jsonbody = "{\"idCountry\":99,\"name\":\"Japan\",\"capital\":\"Tokyo\"}";

		this.mockMvc.perform(put("/updatecountry/{id}", 99)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(10)
	public void test_deleteCountry() throws Exception {
		country = new Country(3, "Japan", "Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);// Mocking

		this.mockMvc.perform(delete("/deletecountry/{id}", countryID))
				.andExpect(status().isOk());
	}

	@Test
	@Order(11)
	public void test_deleteCountry_notFound() throws Exception {
		when(countryService.getCountryById(99)).thenReturn(null);// Mocking

		this.mockMvc.perform(delete("/deletecountry/{id}", 99))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(12)
	public void test_countryExists() throws Exception {
		when(countryService.existsById(1)).thenReturn(true);// Mocking

		this.mockMvc.perform(get("/countryexists/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(content().string("true"));
	}

	@Test
	@Order(13)
	public void test_countCountries() throws Exception {
		when(countryService.countCountries()).thenReturn(2L);// Mocking

		this.mockMvc.perform(get("/countrycount"))
				.andExpect(status().isOk())
				.andExpect(content().string("2"));
	}
}