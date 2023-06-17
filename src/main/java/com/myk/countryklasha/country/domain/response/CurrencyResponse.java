package com.myk.countryklasha.country.domain.response;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CurrencyResponse {
    String name;
    String currency;
    String iso2;
    String iso3;
}
