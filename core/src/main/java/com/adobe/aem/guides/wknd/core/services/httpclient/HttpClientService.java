package com.adobe.aem.guides.wknd.core.services.httpclient;

import com.adobe.aem.guides.wknd.core.beans.ApiResponse;

public interface HttpClientService {

    <T> ApiResponse<T> get(String url, Class<T> responseType);
}
