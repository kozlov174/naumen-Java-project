//package com.naumen.naumenproject.filter;
//
//import com.naumen.naumenproject.repository.RentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class RentFilter implements Filter {
//
//    @Autowired
//    private RentRepository rentRepository;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        System.out.println("Request URI is: " + req.getRequestURI());
//        chain.doFilter(req, res);
//        System.out.println("Response Status Code is: " + res.getStatus());
//    }
//}
