package com.elasticsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by baishuai on 2017/8/24.
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        LOG.info(" application running...");
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(9999);
    }

}
