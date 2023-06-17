package com.myk.countryklasha.country.domain.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class StateResponse {
    String name;
    String iso2;
    String iso3;
    List<CountryStateListResponse> states;

}
