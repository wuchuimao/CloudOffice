package com.wu.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-11 16:53
 * @ Description：
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;
}
