package com.pubcrawl.book;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {
    private String bucketName = "pub-crawl-bucket";
    private String region = "us-east-2";
    private String accessKeyId;
    private String secretAccessKey;
}
