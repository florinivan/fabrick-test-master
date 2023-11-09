package it.client.rest.fabricktest.loginterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
 
import java.io.IOException;
import java.nio.charset.Charset;
/**
 * Intercept Request and Response to log the body and the Headers.
 * Set RequestResponseLoggingInterceptor log level to DEBUG to use this Interceptor.
 * See ConfigurationClass.getRestTemplate().
 *
 *  
 */
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
 
    private final Logger log = LoggerFactory.getLogger(this.getClass());
 
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }
 
    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("request =>");
            log.debug("URL  : {}", request.getURI());
            log.debug("Method   : {}", request.getMethod());
            log.debug("Headers  : {}", request.getHeaders());
            log.debug("Body: {}", new String(body, "UTF-8"));
            log.debug("request end <=");
        }
    }
 
    private void logResponse(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("response =>");
            log.debug("Status Code  : {}", response.getStatusCode());
            log.debug("Status Text  : {}", response.getStatusText());
            log.debug("Header   : {}", response.getHeaders());
            log.debug("Body : {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            log.debug("response <=");
        }
    }
}