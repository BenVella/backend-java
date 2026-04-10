package com.backend;

import com.backend.support.PostgresIntegrationTestSupport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class GameBackendApplicationTests extends PostgresIntegrationTestSupport {

    @MockitoBean
    private JwtDecoder jwtDecoder;

}
