package com.wilburomae.pezeshalms.helpers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public final class HttpTestHelper {

    private static final String TEST_USERNAME = "tester@test.com";
    private static final String TEST_PASSWORD = "123456";
    private static String testerApiKey = null;

    private final ObjectMapper mapper = new ObjectMapper();
    private final MockMvc mockMvc;

    public HttpTestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public <T> ResultActions performPost(String endpoint, T request) throws Exception {
        if (testerApiKey == null) localLogin();
        return mockMvc.perform(post(endpoint)
                .header("Authorization", "Bearer " + testerApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    public ResultActions performGet(String endpoint, Map<String, String> params) throws Exception {
        if (testerApiKey == null) localLogin();
        return mockMvc.perform(get(String.format("%s?%s", endpoint, buildParams(params)))
                .header("Authorization", "Bearer " + testerApiKey));
    }

    public <ID> ResultActions performGetById(String endpoint, ID id, Map<String, String> params) throws Exception {
        return performGet("%s/%s".formatted(endpoint, id), params);
    }

    public <T, ID> ResultActions performPut(String endpoint, ID id, T request) throws Exception {
        if (testerApiKey == null) localLogin();
        return mockMvc.perform(put(String.format("%s/%s", endpoint, id))
                .header("Authorization", "Bearer " + testerApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    public ResultActions performDelete(String endpoint, String id) throws Exception {
        if (testerApiKey == null) localLogin();
        return mockMvc.perform(delete(String.format("%s/%s", endpoint, id))
                .header("Authorization", "Bearer " + testerApiKey)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public MvcResult performLogin(String username, String password) throws Exception {
        String basicAuth = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.UTF_8);
        return mockMvc.perform(get("/authentication/login")
                        .header("Authorization", "Basic " + basicAuth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    public <T> T mapJsonDataToClass(MvcResult result, Class<T> tClass) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.treeToValue(extractDataNode(result), tClass);
    }

    public <T> Collection<T> mapJsonDataToCollectionClass(MvcResult result, Class<T> tClass) throws UnsupportedEncodingException, JsonProcessingException {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, tClass);
        return mapper.treeToValue(extractDataNode(result), collectionType);
    }

    private JsonNode extractDataNode(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        JsonNode tree = mapper.readTree(result.getResponse().getContentAsString());
        return tree.get("data");
    }

    private String buildParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        int end = sb.lastIndexOf("&");

        return end == -1 ? sb.toString() : sb.substring(0, end);
    }

    private void localLogin() throws Exception {
        testerApiKey = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJlbmNyeXB0aW9uLmxtcy5jb20iLCJzdWIiOiJzeXNhZG1pbiIsImV4cCI6MTc2NTMxNTg1NSwiaWF0IjoxNzY1MjI5NDU1LCJhdXRob3JpdGllcyI6WyJST0xFX1JFQURfQUNDT1VOVFMiLCJST0xFX1JFQURfQUNDT1VOVF9TVEFUVVNFUyIsIlJPTEVfV1JJVEVfQUNDT1VOVF9UWVBFUyIsIlJPTEVfV1JJVEVfQ1VSUkVOQ0lFUyIsIlJPTEVfREVMRVRFX0FDQ09VTlRfQkFMQU5DRVMiLCJST0xFX1JFQURfQ1VSUkVOQ0lFUyIsIlJPTEVfUkVBRF9QQVJUTkVSX0FDQ09VTlRTIiwiUk9MRV9ERUxFVEVfUEFSVE5FUl9BQ0NPVU5UUyIsIlJPTEVfREVMRVRFX0FDQ09VTlRfVFlQRVMiLCJST0xFX1dSSVRFX1BBUlRORVJfQUNDT1VOVFMiLCJST0xFX0RFTEVURV9DVVJSRU5DSUVTIiwiUk9MRV9ERUxFVEVfQUNDT1VOVF9TVEFUVVNFUyIsIlJPTEVfUkVBRF9BQ0NPVU5UX1RZUEVTIiwiUk9MRV9XUklURV9BQ0NPVU5UX1NUQVRVU0VTIiwiUk9MRV9ERUxFVEVfQUNDT1VOVFMiLCJST0xFX1dSSVRFX0FDQ09VTlRfQkFMQU5DRVMiLCJST0xFX1dSSVRFX0FDQ09VTlRTIiwiUk9MRV9SRUFEX0FDQ09VTlRfQkFMQU5DRVMiLCJST0xFX0xPR0lOIiwiUk9MRV9ERUxFVEVfVVNFUlMiLCJST0xFX1dSSVRFX1VTRVJTIiwiUk9MRV9ERUxFVEVfUk9MRVMiLCJST0xFX1JFQURfVVNFUlMiLCJST0xFX1dSSVRFX1JPTEVTIiwiUk9MRV9SRUFEX1JPTEVTIiwiUk9MRV9ERUxFVEVfVFJBTlNBQ1RJT05TIiwiUk9MRV9SRUFEX1RSQU5TQUNUSU9OUyIsIlJPTEVfV1JJVEVfVFJBTlNBQ1RJT05fVFlQRV9DT01QT05FTlRTIiwiUk9MRV9SRUFEX1RSQU5TQUNUSU9OX1RZUEVTIiwiUk9MRV9XUklURV9UUkFOU0FDVElPTl9UWVBFUyIsIlJPTEVfREVMRVRFX1RSQU5TQUNUSU9OX0VOVFJJRVMiLCJST0xFX1dSSVRFX1RSQU5TQUNUSU9OX0VOVFJJRVMiLCJST0xFX0RFTEVURV9UUkFOU0FDVElPTl9UWVBFUyIsIlJPTEVfREVMRVRFX1RSQU5TQUNUSU9OX1RZUEVfQ09NUE9ORU5UUyIsIlJPTEVfUkVBRF9UUkFOU0FDVElPTl9FTlRSSUVTIiwiUk9MRV9SRUFEX1RSQU5TQUNUSU9OX1RZUEVfQ09NUE9ORU5UUyIsIlJPTEVfV1JJVEVfVFJBTlNBQ1RJT05TIl0sInVzZXJuYW1lIjoic3lzYWRtaW4ifQ.hTcXMvOIO1jWhy2NjiZD6VxR52c0iJ4zogAXMqfd4H1TacN90kXj422V6hd2jg06LDlfpwyNHzubBn-Z9klRaDchy9xoWfu0XVgvBqWAbkVcMQKPk5X9tc-0xJEt0KMxq_M7VwpEKcsW9wKw7zdOTZuROIplT7a3NGkxhN_qGCcI8Wa_tepEClejSrBQHAsPWHGum8yh1HjzVmxLU1ggFuZvZ4gVN73xkgI1B6chavlGKm8ujLoW8KIzpL43d_ZREWyJPrMLN4g3pDpc2kan69sTsQLQfBlBHqwG-65duqjYuRZLiiiyWN5cIdVU4I9v6bbptXdJwkZ0MeWzXn2w-4rFeVv8DQNG-N52j_b9La4y5a24VJQxALeZ50_xDljpk3dAuTSX7A7eVS7Mo1w0P_eSdPIdggG6Q1MAb7EYS-0ufMPFLT6MhJxopJJNwCRJbgviT3KCoWxjx-riNywWoyH29dpn0AujUlnMdM2Ao2EkUfl4Pz5ch2Mw3gLUz97_QqJjsfBm5LcPe78BeHRs2zlsLEjlnaLmGqd7KKRNZ6WiDG6-bSdhTCV8KTstzHMClB032yYmPXgVMrAoEvHqq1-2-ds_rk0GfajVXCvYM6mNQEWvLbl6o8egYVHFK0RyyAqPNm7nCWjde9rOHVV-PD8fSTqqU3-IzwUfcMK_L-c";
//        MvcResult result = performLogin(TEST_USERNAME, TEST_PASSWORD);
//        testerApiKey = mapJsonDataToClass(result, String.class);
//        Assertions.assertNotNull(testerApiKey);
//        Assertions.assertFalse(testerApiKey.isEmpty());
    }
}
