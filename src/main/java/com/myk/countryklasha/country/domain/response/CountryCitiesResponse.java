package com.myk.countryklasha.country.domain.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CountryCitiesResponse {
    public String city;
    public String country;
    public List<PopulationCountResponse> populationCounts;

}