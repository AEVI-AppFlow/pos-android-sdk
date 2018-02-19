package com.aevi.sdk.flow.util;


import java.io.InputStream;
import java.util.Properties;

public final class ApiVersionProvider {

    private static final String VERSION_PROP = "version";

    private ApiVersionProvider() {
    }

    public static String getApiVersion(String propsFile) {
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream(propsFile);
            properties.load(is);
            return properties.getProperty(VERSION_PROP);
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
