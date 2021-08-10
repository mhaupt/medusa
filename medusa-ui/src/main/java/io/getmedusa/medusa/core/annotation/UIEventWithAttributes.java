package io.getmedusa.medusa.core.annotation;

import io.getmedusa.medusa.core.util.SecurityContext;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.lang.reflect.InvocationTargetException;

public interface UIEventWithAttributes {
    PageAttributes setupAttributes(ServerRequest request, SecurityContext securityContext);
}
