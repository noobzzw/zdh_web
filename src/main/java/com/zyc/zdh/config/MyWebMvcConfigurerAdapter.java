package com.zyc.zdh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Configuration
@EnableWebMvc
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	Environment ev;

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// 前后端分离后无需后端控制跳转404
	@Deprecated
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		// 控制404跳转页面
		return container -> {
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
			container.addErrorPages(error404Page);
		};
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        System.out.println("打印web.path:"+ev.getProperty("web.path"));
		viewResolver.setPrefix(ev.getProperty("web.path"));
		viewResolver.setSuffix(".html");
		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/**").addResourceLocations("/");
		registry.addResourceHandler("/register**","/statics/**","/css/**","/js/**","/fonts/**","/img/**",
				"/plugins/**","/zdh_flow/**","/favicon.ico","/etl/js/**","etl/css/**","/statics/**")
				.addResourceLocations(ev.getProperty("web.path"))
				.addResourceLocations("/statics/")
				.addResourceLocations(ev.getProperty("web.path")+"css/")
				.addResourceLocations(ev.getProperty("web.path")+"js/")
				.addResourceLocations(ev.getProperty("web.path")+"fonts/")
				.addResourceLocations(ev.getProperty("web.path")+"img/")
				.addResourceLocations(ev.getProperty("web.path")+"plugins/")
				.addResourceLocations(ev.getProperty("web.path")+"zdh_flow/")
				.addResourceLocations(ev.getProperty("web.path")+"favicon.ico")
				.addResourceLocations(ev.getProperty("web.path")+"statics/");

	}

}
