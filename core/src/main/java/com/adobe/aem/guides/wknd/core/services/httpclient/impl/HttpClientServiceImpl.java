package com.adobe.aem.guides.wknd.core.services.httpclient.impl;

import com.adobe.aem.guides.wknd.core.beans.ApiResponse;
import com.adobe.aem.guides.wknd.core.services.httpclient.HttpClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;

import java.util.Arrays;

@Slf4j
@Component(service = HttpClientService.class, immediate = true)
public class HttpClientServiceImpl implements HttpClientService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public <T> ApiResponse<T> get(String url, Class<T> responseType) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                final var apiResponse = ApiResponse.<T>builder();
                apiResponse.statusCode(statusCode);

                if (statusCode == 200) {
                    var responseHeaders = response.getAllHeaders();
                    apiResponse.headers(Arrays.copyOf(responseHeaders, responseHeaders.length));
                    var jsonResponse = EntityUtils.toString(response.getEntity());
                    apiResponse.body(MAPPER.readValue(jsonResponse, responseType));
                }
                return apiResponse.build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return ApiResponse.<T>builder().build();
    }
}
