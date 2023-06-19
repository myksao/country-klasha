package com.myk.countryklasha.common;

import com.myk.countryklasha.utils.CountryRetrofitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

import static io.reactivex.rxjava3.schedulers.Schedulers.io;

@Configuration
@Slf4j
public class RetrofitConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CountryRetrofitUtils countryClient() {
        var client =  new Retrofit.Builder()
                .baseUrl("https://countriesnow.space/api/v0.1/countries/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(CountryRetrofitUtils.class);
        log.info("Country client created: {}", client);
        return client;
    }
}
