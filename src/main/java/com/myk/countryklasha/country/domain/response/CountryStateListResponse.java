package com.myk.countryklasha.country.domain.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class CountryStateListResponse {
    String name;
    String state_code;
    List<String> cities;
}
