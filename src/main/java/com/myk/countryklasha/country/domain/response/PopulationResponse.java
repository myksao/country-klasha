package com.myk.countryklasha.country.domain.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PopulationResponse {
    String country;
    String code;
    String iso3;
    List<PopulationCountResponse> populationCountResponseCounts;
}
