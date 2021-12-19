package io.getmedusa.medusa.core.injector.tag;

import io.getmedusa.medusa.core.injector.tag.meta.InjectionResult;
import io.getmedusa.medusa.core.registry.ActiveSessionRegistry;
import io.getmedusa.medusa.core.registry.HydraRegistry;
import io.getmedusa.medusa.core.websocket.hydra.HydraMenuItem;
import io.getmedusa.medusa.core.websocket.hydra.meta.HydraStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class HydraMenuTagTest {

    private static final HydraMenuTag TAG = new HydraMenuTag();
    private static final InjectionResult INPUT = new InjectionResult("""
                    <!DOCTYPE html>
                    <html lang="en">
                    <body>
                    <nav h-menu="example-menu"></nav>
                    </body>
                    </html>""");

    @BeforeEach
    void setup() {
        ActiveSessionRegistry.getInstance().clear();
    }

    @Test
    void testTag() {
        Set<HydraMenuItem> menuItems = new HashSet<>();
        menuItems.add(new HydraMenuItem("https://google.com", "Google.com"));
        menuItems.add(new HydraMenuItem("https://google.be", ""));

        HydraRegistry.update(new HydraStatus(Collections.singletonMap("example-menu", menuItems)));

        final String html = TAG.injectWithVariables(INPUT).getHTML();

        System.out.println(html);

        Assertions.assertFalse(html.contains("nav h-menu=\"example-menu\""));

        Assertions.assertTrue(html.contains("<ul h-menu=\"example-menu\"><li><a href="));
        Assertions.assertTrue(html.contains("<li><a href=\"https://google.com\">Google.com</a></li>"));
        Assertions.assertTrue(html.contains("<li><a href=\"https://google.be\">https://google.be</a></li>"));
        Assertions.assertTrue(html.contains("</li></ul></nav>"));
    }

    @Test
    void testNoItems() {
        HydraRegistry.update(new HydraStatus(new HashMap<>(Map.of("example-menux", new HashSet<>()))));
        final String html = TAG.injectWithVariables(INPUT).getHTML();

        Assertions.assertFalse(html.contains("nav h-menu=\"example-menu\""));

        Assertions.assertTrue(html.contains("<ul h-menu=\"example-menu\">"));
        Assertions.assertTrue(html.contains("</ul></nav>"));
    }
}
