package com.myk.countryklasha;

import com.myk.countryklasha.country.controller.CountryController;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CountryKlashaApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertThat(countryController).isNotNull();
    }

    @Test
    public void shouldfetchTopCities() {
        try {
            verifyAsyncGetRequest("/api/v1/country/2", HttpStatus.OK, null);
            mockMvc.perform(get("/api/v1/country/ggg")).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldfetchCountryInfo() {
        try {
            verifyAsyncGetRequest("/api/v1/country/info/nidddd", HttpStatus.NOT_FOUND, null);
            verifyAsyncGetRequest("/api/v1/country/info/nigeria", HttpStatus.OK, null);
        } catch (Exception e) {
           //  throw new CustomException();
        }
    }

    @Test
    public void shouldfetchCountryStateAndCities() {
        try {
            verifyAsyncGetRequest("/api/v1/country/states/cities?country=nidddd", HttpStatus.NOT_FOUND, null);
            verifyAsyncGetRequest("/api/v1/country/states/cities?country=nigeria", HttpStatus.OK, null);
        } catch (Exception e) {
           //  throw new CustomException();
        }
    }

    private void verifySyncGetRequest(
            String request, HttpStatus expectedStatus,  String expectedContent) throws Exception {
        doGet(request)
                .andExpect(status().is(expectedStatus.value()));
                // .andExpect(content().string(expectedContent));
    }

    private void verifyAsyncGetRequest(
            String request, HttpStatus expectedStatus, @Nullable String expectedContent) throws Exception {
        mockMvc
                .perform(asyncDispatch(doGet(request).andReturn()))
                .andExpect(status().is(expectedStatus.value()));
                // .andExpect(content().string(expectedContent));
    }

    private ResultActions doGet(String request) throws Exception {
        return mockMvc.perform(get(request));
    }
}
