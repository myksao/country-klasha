package com.myk.countryklasha.utils;

import com.myk.countryklasha.country.domain.payload.CountryCitiesPopulationPayload;
import com.myk.countryklasha.country.domain.payload.CountryPayload;
import com.myk.countryklasha.country.domain.response.*;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface CountryRetrofitUtils {
    /**
     * 1. Fetch top cities for Italy, New Zealand and Ghana
     */
    @POST("population/cities/filter")
    Single<CountryResponse<List<CountryCitiesResponse>>> fetchTopCities(@Body CountryCitiesPopulationPayload payload);


    /**
     * 2. Fetch population, capital, position and currency of a country
     */
    @POST("population")
    Single<CountryResponse<PopulationResponse>> fetchPopulation(@Body CountryPayload payload);

    @POST("capital")
    Single<CountryResponse<CapitalResponse>> fetchCapital(@Body CountryPayload payload);

    @POST("positions")
    Single<CountryResponse<PositionResponse>> fetchPosition(@Body CountryPayload payload);

    @POST("currency")
    Single<CountryResponse<CurrencyResponse>> fetchCurrency(@Body CountryPayload payload);

    @POST("iso")
    Single<CountryResponse<IsoResponse>> fetchIso(@Body CountryPayload payload);


    /**
     * 3. Fetch states and cities of a country
     */
    @POST("states")
    Single<CountryResponse<StateResponse>> fetchStates(@Body CountryPayload payload);

    @POST("state/cities")
    Single<CountryResponse<List<String>>> fetchCities(@Body CountryPayload payload);

}
