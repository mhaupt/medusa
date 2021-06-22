package io.getmedusa.medusa;

import io.getmedusa.medusa.core.annotation.PageSetup;
import io.getmedusa.medusa.core.annotation.UIEvent;
import io.getmedusa.medusa.core.annotation.UIEventController;
import io.getmedusa.medusa.core.injector.DOMChange;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Page2EventHandler implements UIEventController {

    @Override
    public PageSetup setupPage() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("example-value", UUID.randomUUID().toString());
        return new PageSetup(
                "/page2",
                "page2",
                modelMap);
    }

}
