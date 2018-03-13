package com.aevi.sdk.flow.util;


import java.io.InputStream;
import java.util.Properties;

public final class ApiVersionProvider {

    private static final String VERSION_PROP = "version";

    private ApiVersionProvider() {
    }

    public static String getApiVersion(String propsFile) {
        return readProperty(getPropsFileInputStream(propsFile));
    }

    static String readProperty(InputStream is) {
        try {
            Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(VERSION_PROP);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    static InputStream getPropsFileInputStream(String propsFile) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(propsFile);
    }
}
