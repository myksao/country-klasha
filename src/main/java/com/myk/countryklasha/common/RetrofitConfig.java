package com.myk.countryklasha.common;

import com.myk.countryklasha.utils.CountryRetrofitUtils;
import com.myk.countryklasha.utils.ExchangeRetrofitUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static io.reactivex.rxjava3.schedulers.Schedulers.io;

@Configuration
@Slf4j
public class RetrofitConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CountryRetrofitUtils countryClient() {
        Scheduler observeOn = Schedulers.computation();

        var client =  new Retrofit.Builder()
                .baseUrl("https://countriesnow.space/api/v0.1/countries/")
                //.addCallAdapterFactory(new ObserveOnMainCallAdapterFactory(observeOn))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(CountryRetrofitUtils.class);
        log.info("Country client created: {}", client);
        return client;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ExchangeRetrofitUtils exchangeClient() {
        var client =  new Retrofit.Builder()
                .baseUrl("https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fcf9dbe9-a416-46fc-ab09-fc76f881f46e/")
                .addConverterFactory(JaxbConverterFactory.create())
                .build()
                .create(ExchangeRetrofitUtils.class);
        log.info("exchange client created: {}", client);
        return client;
    }



    static final class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {
        final Scheduler scheduler;

        ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public @Nullable CallAdapter<?, ?> get(
                Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (getRawType(returnType) != Observable.class) {
                return null; // Ignore non-Observable types.
            }

            // Look up the next call adapter which would otherwise be used if this one was not present.
            //noinspection unchecked returnType checked above to be Observable.
            final CallAdapter<Object, Observable<?>> delegate =
                    (CallAdapter<Object, Observable<?>>)
                            retrofit.nextCallAdapter(this, returnType, annotations);

            return new CallAdapter<Object, Object>() {
                @Override
                public Object adapt(Call<Object> call) {
                    // Delegate to get the normal Observable...
                    Observable<?> o = delegate.adapt(call);
                    // ...and change it to send notifications to the observer on the specified scheduler.
                    return o.observeOn(scheduler);
                }

                @Override
                public Type responseType() {
                    return delegate.responseType();
                }
            };
        }
    }
}
