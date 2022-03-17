package com.nextop.operatelog;

import com.nextop.operatelog.configuration.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmsProperties.class)//使@ConfigurationProperties注解生效
public class SmsAutoConfiguration {
    @Bean
    public SmsService getBean(SmsProperties smsProperties){
        SmsService smsService = new SmsService(smsProperties);
        return smsService;
    }
}