//package com.naumen.naumenproject.config;
//
//import com.naumen.naumenproject.filter.RentFilter;
//import com.naumen.naumenproject.repository.RentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<RentFilter> filter() {
//        FilterRegistrationBean<RentFilter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new RentFilter());
//        bean.addUrlPatterns("/rent/*");
//        return bean;
//    }
//
//}
