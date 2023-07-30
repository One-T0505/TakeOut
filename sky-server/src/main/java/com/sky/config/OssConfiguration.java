package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ymy
 * 2023/7/30 - 11 : 47
 * 配置类，用于创建AliOssUtil对象
 **/

@Configuration
@Slf4j
public class OssConfiguration {

    // 因为 AliOssProperties 类上有 @Component 和 @ConfigurationProperties(prefix = "sky.alioss")
    // 所以 AliOssProperties 已经在spring 容器中了，我们可以直接用，不需要 Autowired 也可以自动匹配
    @Bean
    @ConditionalOnMissingBean  // 没有这个工具类的时候才创建，不需要多个
    public AliOssUtil ossUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象: {}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
