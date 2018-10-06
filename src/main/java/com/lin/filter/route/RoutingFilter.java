package com.lin.filter.route;

import com.lin.filter.EatuulFilter;
import com.lin.http.RequestContext;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lin on 2018/10/6.
 */
public class RoutingFilter implements EatuulFilter {
    @Override
    public void run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        RequestEntity requestEntity = ctx.getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, byte.class);
        ctx.setResponseEntity(responseEntity);
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "route";
    }
}
