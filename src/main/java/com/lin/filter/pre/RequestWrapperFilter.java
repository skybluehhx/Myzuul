package com.lin.filter.pre;

import com.lin.filter.EatuulFilter;
import com.lin.http.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * Created by lin on 2018/10/6.
 * 前置过滤器，负责封装请求
 */
public class RequestWrapperFilter implements EatuulFilter {

    @Override
    public void run() {
        String rootURL = "http://localhost:9090";
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String targetURL = rootURL + request.getRequestURI();
        RequestEntity<byte[]> requestEntity = null;
        try {
            requestEntity = createRequestEntity(request, targetURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将请求体requestEntity放置全局对象当中
        ctx.setRequestEntity(requestEntity);
    }

    private RequestEntity createRequestEntity(HttpServletRequest request, String url) throws IOException, URISyntaxException {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        //1.封装请求头
        MultiValueMap<String, String> headers = createRequestHeaders(request);
        //2.封装请求体
        byte[] body = createRequestBody(request);
        //3.构造出RestTemplate能识别的RequestEntity
        RequestEntity requestEntity = new RequestEntity<byte[]>(body, headers, httpMethod, new URI(url));
        return requestEntity;
    }


    public MultiValueMap<String, String> createRequestHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headreValue : headerValues) {
                headers.add(headerName, headreValue);
            }
        }
        return headers;
    }

    public byte[] createRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        //作用
        return StreamUtils.copyToByteArray(inputStream);
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public String filterType() {
        return "pre";
    }
}
