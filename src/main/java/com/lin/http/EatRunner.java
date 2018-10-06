package com.lin.http;

import com.lin.filter.EatuulFilter;
import com.lin.filter.post.SendResponseFilter;
import com.lin.filter.pre.RequestWrapperFilter;
import com.lin.filter.route.RoutingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lin on 2018/10/6.
 */
public class EatRunner {

    private ConcurrentHashMap<String, List<EatuulFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<EatuulFilter>>() {{
        put("pre", new ArrayList<EatuulFilter>() {
            {
                add(new RequestWrapperFilter());
            }
        });
        put("route", new ArrayList<EatuulFilter>() {
            {
                add(new RoutingFilter());
            }
        });
        put("post", new ArrayList<EatuulFilter>() {
            {
                add(new SendResponseFilter());
            }
        });

    }};

    public void init(HttpServletRequest request, HttpServletResponse response) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setRequest(request);
        ctx.setResponse(response);
    }


    public void preRoute() throws Throwable {
     runFilters("pre");
    }

    public void route() throws Throwable {
        runFilters("route");
    }

    public void postRoute() throws Throwable {
        runFilters("post");
    }

    public void runFilters(String sType) throws Throwable {
        List<EatuulFilter> list = this.hashFiltersByType.get(sType);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                EatuulFilter zuuFilter = list.get(i);
                zuuFilter.run();
            }
        }
    }
}
