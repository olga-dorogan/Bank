package com.custom.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by olga on 18.09.15.
 */
@WebFilter(filterName = "responseType", urlPatterns = {"/*"})
public class ResponseTypeFilter implements Filter {
    private static final String RESP_CONTENT_TYPE = "application/json;charset=utf-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        servletResponse.setContentType(RESP_CONTENT_TYPE);
    }

    @Override
    public void destroy() {

    }
}
