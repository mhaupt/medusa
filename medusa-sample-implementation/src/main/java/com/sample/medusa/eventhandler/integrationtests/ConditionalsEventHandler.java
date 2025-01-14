package com.sample.medusa.eventhandler.integrationtests;

import io.getmedusa.medusa.core.annotation.PageAttributes;
import io.getmedusa.medusa.core.annotation.UIEventPage;
import io.getmedusa.medusa.core.injector.DOMChanges;
import io.getmedusa.medusa.core.util.SecurityContext;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

import static io.getmedusa.medusa.core.injector.DOMChanges.of;

@UIEventPage(path = "/test/conditionals", file = "pages/integration-tests/conditionals.html")
public class ConditionalsEventHandler {

    int counter = 0;

    public PageAttributes setupAttributes(ServerRequest request, SecurityContext securityContext) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("counter-value", counter);
        return new PageAttributes(modelMap);
    }

    public DOMChanges increaseCounter(int parameter) {
        counter += parameter;
        if(counter > 10) {
            counter = 0;
        }
        return of("counter-value", counter);
    }
}
