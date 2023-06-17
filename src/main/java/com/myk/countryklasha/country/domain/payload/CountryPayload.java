package com.myk.countryklasha.country.domain.payload;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class CountryPayload {
    @NonNull String country;
    String state;
}
