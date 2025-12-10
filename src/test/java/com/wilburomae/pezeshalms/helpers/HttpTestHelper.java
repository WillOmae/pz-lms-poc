package com.wilburomae.pezeshalms.helpers;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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
        return mockMvc.perform(post("/authentication/login")
                        .header("Authorization", "Basic " + basicAuth))
                .andReturn();
    }

    public <T> T mapJsonDataToClass(MvcResult result, Class<T> tClass) throws UnsupportedEncodingException {
        return mapper.treeToValue(extractDataNode(result), tClass);
    }

    public <T> Collection<T> mapJsonDataToCollectionClass(MvcResult result, Class<T> tClass) throws UnsupportedEncodingException {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, tClass);
        return mapper.treeToValue(extractDataNode(result), collectionType);
    }

    private JsonNode extractDataNode(MvcResult result) throws UnsupportedEncodingException {
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
        MvcResult result = performLogin("system-admin@test-lms.xyz", "123456");
        testerApiKey = mapJsonDataToClass(result, String.class);
        Assertions.assertNotNull(testerApiKey);
        Assertions.assertFalse(testerApiKey.isEmpty());
    }
}
