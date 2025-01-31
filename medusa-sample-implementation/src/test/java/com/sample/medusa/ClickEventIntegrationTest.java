package com.sample.medusa;

import com.sample.medusa.meta.AbstractSeleniumTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClickEventIntegrationTest extends AbstractSeleniumTest {

    @Test
    void testClickEventWorks() {
        goTo("/test/click-event");
        Assertions.assertTrue(driver.getPageSource().contains("Hello click event"));

        Assertions.assertEquals("0", getFromValue("my-counter"));
        clickById("my-counter-btn");
        Assertions.assertEquals("1", getFromValue("my-counter"));
    }

}
