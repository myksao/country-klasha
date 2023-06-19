package com.myk.countryklasha.country.controller;

import com.myk.countryklasha.annotation.ValuesAllowed;
import com.myk.countryklasha.common.SuccessResponse;
import com.myk.countryklasha.country.domain.response.CountryCitiesResponse;
import com.myk.countryklasha.country.domain.response.CountryResponse;
import com.myk.countryklasha.country.domain.response.StateResponse;
import com.myk.countryklasha.country.dto.CountryInfoDto;
import com.myk.countryklasha.country.dto.PopulationResponseDto;
import com.myk.countryklasha.country.service.CountryService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
@Validated
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
    public ResponseEntity<Single<SuccessResponse>> fetchCountryMonetaryInfo(
            @RequestParam(name = "country") @NotBlank String country,
            @RequestParam(name = "amount") @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
            @RequestParam(name = "target_currency")
            @ValuesAllowed(values = {
                    "NGN",
                    "UGX"
            }) String target_currency)  {
        var data = this.countryService.fetchCountryMonetaryInfo(country, amount, target_currency);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
