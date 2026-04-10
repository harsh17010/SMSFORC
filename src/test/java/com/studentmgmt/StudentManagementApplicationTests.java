package com.studentmgmt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * APPLICATION TEST
 * ==================
 * Basic test to verify the Spring application context loads successfully.
 *
 * @SpringBootTest loads the full application context.
 * @ActiveProfiles("h2") ensures tests use the H2 in-memory database,
 * not MySQL (which might not be available in test/CI environments).
 */
@SpringBootTest
@ActiveProfiles("h2")
class StudentManagementApplicationTests {

    /**
     * This test simply verifies that the application context
     * can start without errors. If any bean is misconfigured,
     * this test will fail.
     */
    @Test
    void contextLoads() {
        // If the application context loads, the test passes
    }
}
