package pl.edu.pjwstk.lab11.service;

import java.util.List;

import pl.edu.pjwstk.lab11.domain.City;
import pl.edu.pjwstk.lab11.domain.Country;

public interface CountryManager {

	// Country
	void addCountry(Country country);
	void deleteCountry(Country country);
	void updateCountry(Country country);
	Country findCountryById(int id);
	List<Country> getAllCountries();

	// City
	Long addCity(City city);
	void deleteCity(City city);
	void updateCity(City city);
	City findCityByPostal_code(String postal_code);
	City findCityById(int id);
	List<City> getAllCities();

	void findCityForCountry(Long idCountry1, Long idCountry2, Long idCity);
}