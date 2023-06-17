package com.myk.countryklasha.country.domain.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CountryCitiesPopulationPayload {
    int limit;
    String order;
    String orderBy;
    String country;
}
