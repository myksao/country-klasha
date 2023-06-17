package com.myk.countryklasha.country.controller;

import com.myk.countryklasha.common.SuccessResponse;
import com.myk.countryklasha.country.domain.response.CountryCitiesResponse;
import com.myk.countryklasha.country.domain.response.CountryResponse;
import com.myk.countryklasha.country.domain.response.StateResponse;
import com.myk.countryklasha.country.dto.CountryInfoDto;
import com.myk.countryklasha.country.dto.PopulationResponseDto;
import com.myk.countryklasha.country.service.CountryService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService countryService;

    @GetMapping(path = "{limit}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Single<SuccessResponse>> fetchTopCities(@PathVariable("limit") @Min(2) @NotBlank int limit)  {
        var data = this.countryService.fetchTopCities(limit);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping(path = "info/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Single<SuccessResponse>> fetchCountryInfo(@PathVariable("country") @NotBlank String country)  {
        var data = this.countryService.fetchCountryInfo(country);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping(path = "states/cities/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Single<SuccessResponse>> fetchCountryStateAndCities(@PathVariable("country") @NotBlank String country)  {
        var data = this.countryService.fetchCountryStateAndCities(country);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping(path = "info/monetary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Single<Map<String, String>>> fetchCountryMonetaryInfo(@RequestParam(name = "country") @NotBlank String country, @RequestParam(name = "amount") @NotBlank int amount)  {
        var data = this.countryService.fetchCountryMonetaryInfo(country, amount);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
