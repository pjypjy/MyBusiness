package com.myBusiness.products.restaurant.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class StringFilter implements Filter {

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Request paramsRequest = new Request((HttpServletRequest) servletRequest);
        filterChain.doFilter(paramsRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    class Request extends HttpServletRequestWrapper{

        public Request(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String v = trim(super.getParameter(name) ,this);
            if(v != null){
                v = v.trim();
            }
            return v ;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if(values == null) return null;
            for (int i = 0; i < values.length; i++) {
                values[i] = trim(values[i] ,this);
            }
            return values;
        }

        public String trim(String value ,HttpServletRequest request) {
            if (value == null) {
                return null;
            }
            return value.trim();
        }
    };
}
