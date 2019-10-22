package com.demo.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * PropertiesUtils
 *
 * @author gc
 */
public class PropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Properties readProperties(String filePath, String fileName) {
        Properties prop = new Properties();
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath + File.separator + fileName);
            InputStream in = classPathResource.getInputStream();

            // 设置UTF-8 支持中文
            Reader reader = new InputStreamReader(in, "UTF-8");
            prop.load(reader);
            if (null != in) {
                in.close();
            }
        } catch (Exception e) {
            logger.error("获取配置文件" + filePath + "失败 " + e.getMessage(), e);
        }
        return prop;
    }

}
