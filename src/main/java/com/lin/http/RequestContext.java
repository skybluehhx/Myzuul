package com.lin.http;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lin on 2018/10/6.
 */
public class RequestContext extends ConcurrentHashMap {
    protected static Class<? extends RequestContext> contextClass = RequestContext.class;

    protected static final ThreadLocal<? extends RequestContext> threadLocal =
            new ThreadLocal<RequestContext>() {
               @Override
                protected RequestContext initialValue() {
                    try {
                        return contextClass.newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }
            };


    public HttpServletRequest getRequest() {
        return (HttpServletRequest) get("request");
    }

    public void setRequest(HttpServletRequest request) {
        put("request", request);
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) get("response");
    }
    public void setResponse(HttpServletResponse response) {
        put("response", response);
    }

    public RequestEntity getRequestEntity() {
        return (RequestEntity) get("requestEntity");
    }

    public void setRequestEntity(RequestEntity requestEntity) {
        put("requestEntity", requestEntity);
    }

    public ResponseEntity getResponseEntity() {
        return (ResponseEntity) get("responseEntity");
    }
    public void setResponseEntity(ResponseEntity responseEntity) {
        put("responseEntity", responseEntity);
    }



    public static RequestContext getCurrentContext() {
        RequestContext context = threadLocal.get();
        return context;
    }

    public void set(String key, Object value) {
        if (value != null) {
            put(key, value);
        } else {
            remove(key);
        }
    }

    public void unset() {
        threadLocal.remove();
    }


}
