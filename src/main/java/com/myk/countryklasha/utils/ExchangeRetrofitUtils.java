package com.myk.countryklasha.utils;

import com.myk.countryklasha.country.domain.payload.CountryPayload;
import com.myk.countryklasha.country.domain.response.CountryResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;

import java.util.List;

public interface ExchangeRetrofitUtils {

    /**
     * 4. Fetch monetary value of a country
     */
    @GET("exchange_rate.csv")
    Single<CountryResponse<List<String>>> fetchCountryMonetary();
}
