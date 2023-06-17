package com.myk.countryklasha.country.service;

import com.myk.countryklasha.common.RetrofitConfig;
import com.myk.countryklasha.common.SuccessResponse;
import com.myk.countryklasha.country.domain.payload.CountryPayload;
import com.myk.countryklasha.country.domain.response.CountryCitiesResponse;
import com.myk.countryklasha.country.domain.payload.CountryCitiesPopulationPayload;
import com.myk.countryklasha.country.domain.response.CountryResponse;
import com.myk.countryklasha.country.domain.response.StateResponse;
import com.myk.countryklasha.country.dto.CountryInfoDto;
import com.myk.countryklasha.country.dto.PopulationResponseDto;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

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
                            retrofitConfig.countryClient().fetchTopCities(italy),
                            retrofitConfig.countryClient().fetchTopCities(newZealand),
                            retrofitConfig.countryClient().fetchTopCities(ghana),
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
                            retrofitConfig.countryClient().fetchPopulation(payload),
                            retrofitConfig.countryClient().fetchCapital(payload),
                            retrofitConfig.countryClient().fetchPosition(payload),
                            retrofitConfig.countryClient().fetchCurrency(payload),
                            retrofitConfig.countryClient().fetchIso(payload),
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

    public Single<Map<String, String>> fetchCountryMonetaryInfo(String country, int amount)  {
            var payload = new CountryPayload(country);
            return Single.zip(
                            retrofitConfig.countryClient().fetchCurrency(payload),
                            retrofitConfig.exchangeClient().fetchCountryMonetary(),
                            (a, b) -> Map.of(
                                    "currency", "a.data",
                                    "iso", " b.data"
                            )
                    ).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single());
    }

}
