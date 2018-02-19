package com.aevi.payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestEnvironment {

    private static final String BASE_TEST_DIR = "src/test/resources/";
    private static final String VALID_RESPONSE = "valid_response.json";
    private static final String ERROR_RESPONSE = "error_response.json";

    private Reader doGetInputStream(String configFile, String basePath) throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
        // Android studio work around
        if (inputStream == null) {
            inputStream = new FileInputStream(new File(basePath, configFile));
        }
        return new InputStreamReader(inputStream);
    }

    String getValidResponseJson() throws IOException {
        return TestEnvironment.readFile(BASE_TEST_DIR + VALID_RESPONSE);
    }

    String getErrorResponseJson() throws IOException {
        return TestEnvironment.readFile(BASE_TEST_DIR + ERROR_RESPONSE);
    }

    private static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
}
