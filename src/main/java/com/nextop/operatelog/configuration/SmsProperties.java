package com.nextop.operatelog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms-config")
public class SmsProperties {
    private String appid;
    private String accountSid;
    private String authToken;
}
