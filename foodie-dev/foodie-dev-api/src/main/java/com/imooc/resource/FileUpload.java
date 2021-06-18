package com.imooc.resource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by guoming.zhang on 2021/3/25.
 */


@Getter
@Setter
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-dev.properties")
@Component
public class FileUpload {
    private String imageUserFaceLocation;

    private String imageServerUrl;
}
