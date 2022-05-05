package com.ccw.project.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Vertification image class
 */
@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // Border
        properties.setProperty("kaptcha.border", "yes");
        // Border Color
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // Font Color
        properties.setProperty("kaptcha.textproducer.font.color", "red");
        // Image Width
        properties.setProperty("kaptcha.image.width", "110");
        // Image Height
        properties.setProperty("kaptcha.image.height", "40");
        // Font size
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        // session key
        properties.setProperty("kaptcha.session.key", "code");
        // Code Length
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // Font Type
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }

}
