package com.aevi.sdk.flow.util;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ApiVersionProviderTest {

    @Test
    public void shouldReadPropertyFromInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("version=1.2.3".getBytes());
        assertThat(ApiVersionProvider.readProperty(inputStream)).isEqualTo("1.2.3");
    }
}
