package com.paazl.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.paazl.SpringWebservicesTestCaseApplication;

@Configuration
@ComponentScan(basePackageClasses = SpringWebservicesTestCaseApplication.class)
public class ApplicationConfiguration {}