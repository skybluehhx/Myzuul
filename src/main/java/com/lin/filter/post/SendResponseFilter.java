package com.lin.filter.post;

import com.lin.filter.EatuulFilter;
import com.lin.http.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by lin on 2018/10/6.
 */
public class SendResponseFilter implements EatuulFilter {
    @Override
    public void run()  {
        addResponseHeaders();
        try {
            writeResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public String filterType() {
        return "post";
    }

    private void addResponseHeaders() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        ResponseEntity responseEntity = ctx.getResponseEntity();
        HttpHeaders httpHeaders = responseEntity.getHeaders();
        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            String headerName = entry.getKey();
            List<String> headValues = entry.getValue();
            for (String headValue : headValues) {
                response.addHeader(headerName, headValue);
            }
        }

    }

    private void writeResponse() throws Exception {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8");
        }
        ResponseEntity responseEntity = ctx.getResponseEntity();
        if (responseEntity.hasBody()) {
            byte[] body = (byte[]) responseEntity.getBody();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(body);
            outputStream.flush();
        }
    }
}
