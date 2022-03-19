package com.nextop.operatelog;

import com.nextop.operatelog.configuration.NextopOperateLogProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
@EnableConfigurationProperties(NextopOperateLogProperties.class)//使@ConfigurationProperties注解生效
public class NextopOperateLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public LogRecordService logRecordService(){
        return new DefaultLogRecordServiceImpl();
    }
}