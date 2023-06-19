package com.myk.countryklasha.country.service;

import com.myk.countryklasha.common.CustomException;
import com.myk.countryklasha.common.RetrofitConfig;
import com.myk.countryklasha.common.SuccessResponse;
import com.myk.countryklasha.country.domain.payload.CountryCitiesPopulationPayload;
import com.myk.countryklasha.country.domain.payload.CountryPayload;
import com.myk.countryklasha.country.domain.response.CountryCitiesResponse;
import com.myk.countryklasha.country.dto.CountryInfoDto;
import com.myk.countryklasha.country.dto.PopulationResponseDto;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {

    private final RetrofitConfig retrofitConfig;

    public Single<SuccessResponse> fetchTopCities(int limit)  {

            var italy = new CountryCitiesPopulationPayload(limit, "dsc", "value", "Italy");
            var newZealand = new CountryCitiesPopulationPayload(limit, "dsc", "value", "New Zealand");
            var ghana = new CountryCitiesPopulationPayload(limit, "dsc", "value", "Ghana");

            return Single.zip(
                            retrofitConfig.countryClient().fetchTopCities(italy).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchTopCities(newZealand).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchTopCities(ghana).subscribeOn(Schedulers.newThread()),
                            (a, b, c) -> {
                                var response = new SuccessResponse();
                                response.success = true;
                                response.message = "Top cities fetched successfully";
                                response.data = new PopulationResponseDto<CountryCitiesResponse>(a.data, b.data, c.data);
                                return response;
                            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single());

    }

    public Single<SuccessResponse> fetchCountryInfo(String country)  {

            var payload = new CountryPayload(country);

            return Single.zip(
                            retrofitConfig.countryClient().fetchPopulation(payload).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchCapital(payload).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchPosition(payload).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchCurrency(payload).subscribeOn(Schedulers.newThread()),
                            retrofitConfig.countryClient().fetchIso(payload).subscribeOn(Schedulers.newThread()),
                            (a, b, c, d, e) -> {
                                // log.info("a: {}", a);
                                var response = new SuccessResponse();
                                response.success = true;
                                response.message = "Country info fetched successfully";
                                response.data = new CountryInfoDto(
                                        country,
                                        Map.of(
                                                "population", a.data,
                                                "capital", b.data,
                                                "position", c.data,
                                                "currency", d.data,
                                                "iso", e.data
                                        )
                                );
                                return response;
                            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single());

    }

    public Single<SuccessResponse> fetchCountryStateAndCities(String country)  {
            var payload = new CountryPayload(country);
            return retrofitConfig.countryClient()
                    .fetchStates(payload)
                    .flatMap(
                            stateResponse -> {
                                // log.info("stateResponse: {}", stateResponse);
                                stateResponse.data.setStates(
                                        stateResponse.data.getStates().parallelStream().map(
                                                state -> {
                                                    var statePayload = new CountryPayload(country, state.getName().equals("Lagos State") ? "Lagos" : state.getName());
                                                    state.setCities(this.retrofitConfig.countryClient().fetchCities(statePayload).blockingGet().data);
                                                    return state;
                                                }
                                        ).toList()
                                );
                                var response = new SuccessResponse();
                                response.success = true;
                                response.message = "Country state and cities fetched successfully";
                                response.data = stateResponse;
                                return Single.just(response);
                            }
                    ).subscribeOn(Schedulers.io()).observeOn(Schedulers.single());
    }

    public Single<SuccessResponse> fetchCountryMonetaryInfo(String country, BigDecimal amount, String target_currency) {
        var payload = new CountryPayload(country);
        return retrofitConfig.countryClient().fetchCurrency(payload).flatMap(
                currencyResponse -> {
                    if (!List.of("USD", "EUR", "JPY", "GBP").contains(currencyResponse.data.getCurrency())) {
                        return Single.error(new CustomException(String.format(
                                "Country monetary info not available for %s", currencyResponse.data.getCurrency()
                        ), HttpStatus.BAD_REQUEST, "Country monetary info not available"));
                    }
                    return currencyValue(currencyResponse.data.getCurrency(), target_currency, amount);
                }
        ).subscribeOn(Schedulers.io()).observeOn(Schedulers.single());
    }

    private Single<SuccessResponse> currencyValue(String countryCurrency, String target_currency, BigDecimal amount) {
        Map<String, BigDecimal> exchange_rate = Map.of(
                "EUR-NGN", BigDecimal.valueOf(493.06),
                "USD-NGN", BigDecimal.valueOf(460.72),
                "JPY-NGN", BigDecimal.valueOf(3.28),
                "GBP-NGN", BigDecimal.valueOf(570.81),
                "EUR-UGX", BigDecimal.valueOf(4.00),
                "USD-UGX", BigDecimal.valueOf(3.00),
                "JPY-UGX", BigDecimal.valueOf(26.62),
                "GBP-UGX", BigDecimal.valueOf(4.00)

        );


        var currencyValue = amount.divide(
                exchange_rate.get(String.format("%s-%s", countryCurrency, target_currency)), 2, RoundingMode.UP
        );
        var response = new SuccessResponse();
        response.success = true;
        response.message = "Country monetary info fetched successfully";
        response.data = Map.of(
                "country_currency", countryCurrency,
                "target_currency", target_currency,
                "amount", currencyValue
        );

        return Single.just(response);
    }

}
