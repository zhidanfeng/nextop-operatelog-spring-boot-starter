package com.nextop.operatelog;

import com.nextop.operatelog.configuration.SmsProperties;
import com.nextop.operatelog.dto.SendSMSDTO;

public class SmsService {

    private String appid;
    private String accountSid;
    private String authToken;

    /**
     * 初始化
     */
    public SmsService(SmsProperties smsProperties) {
        this.appid = smsProperties.getAppid();
        this.accountSid = smsProperties.getAccountSid();
        this.authToken = smsProperties.getAuthToken();
    }

    /**
     * 单独发送
     */
    public String sendSMS(SendSMSDTO sendSMSDTO){
        return "sendSMS";
    }

    /**
     * 群体发送
     */
    public String sendBatchSMS(SendSMSDTO sendSMSDTO){
        return "sendBatchSMS";
    }
}
