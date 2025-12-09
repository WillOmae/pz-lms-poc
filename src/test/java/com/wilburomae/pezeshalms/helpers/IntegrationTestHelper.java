package com.wilburomae.pezeshalms.helpers;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Getter
@Component
public class IntegrationTestHelper {

    private final HttpTestHelper httpTestHelper;

    public IntegrationTestHelper(HttpTestHelper httpTestHelper) {
        this.httpTestHelper = httpTestHelper;
    }

    public <T, U> U create(String endpoint, T body, Class<U> responseClass, HttpStatus expectedStatus) throws Exception {
        MvcResult result = httpTestHelper.performPost(endpoint, body)
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();
        return httpTestHelper.mapJsonDataToClass(result, responseClass);
    }

    public <T> MvcResult createNoAssertion(String endpoint, T body) throws Exception {
        return httpTestHelper.performPost(endpoint, body).andReturn();
    }

    public <U> Collection<U> fetch(String endpoint, Map<String, String> params, Class<U> responseClass, HttpStatus expectedStatus) throws Exception {
        MvcResult result = httpTestHelper.performGet(endpoint, params)
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();
        return httpTestHelper.mapJsonDataToCollectionClass(result, responseClass);
    }

    public <U, ID> U fetchById(String endpoint, ID id, Map<String, String> params, Class<U> responseClass, HttpStatus expectedStatus) throws Exception {
        MvcResult result = httpTestHelper.performGetById(endpoint, id, params)
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();
        return httpTestHelper.mapJsonDataToClass(result, responseClass);
    }

    public <T, ID, U> U update(String endpoint, ID id, T body, Class<U> responseClass, HttpStatus expectedStatus) throws Exception {
        MvcResult result = httpTestHelper.performPut(endpoint, id, body)
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();
        return httpTestHelper.mapJsonDataToClass(result, responseClass);
    }

    public <U, ID> U delete(String endpoint, ID id, Class<U> responseClass, HttpStatus expectedStatus) throws Exception {
        MvcResult result = httpTestHelper.performDelete(endpoint, String.valueOf(id))
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();
        return httpTestHelper.mapJsonDataToClass(result, responseClass);
    }

}
