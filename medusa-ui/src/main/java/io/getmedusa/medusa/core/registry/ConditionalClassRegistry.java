package io.getmedusa.medusa.core.registry;

import io.getmedusa.medusa.core.util.IdentifierGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps a singleton instance with all conditions and their respective ids.
 * As such we can do a lookup if certain variable change would have impact on a condition, and by consequence require re-interpretation of the conditional class.
 * Gets instantiated and filled up with values during beforeInitialization post-processing, like most registries.
 */
public class ConditionalClassRegistry {

    private static final ConditionalClassRegistry INSTANCE = new ConditionalClassRegistry();
    private static final String PREFIX = "$";

    private ConditionalClassRegistry() { }
    public static ConditionalClassRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<String, String> registry = new HashMap<>();

    public String add(String condition) {
        final String key = IdentifierGenerator.generateClassConditionalID(condition);
        registry.put(key, condition);
        return key;
    }

    public List<String> findByConditionField(String fieldWithChange) {
        final String prefixedField = PREFIX + fieldWithChange;
        final List<String> ids = new ArrayList<>();

        for (Map.Entry<String, String> entry : registry.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.contains(prefixedField)) {
                ids.add(key);
            }
        }
        return ids;
    }

    public String get(String impactedDivId) {
        return registry.get(impactedDivId);
    }
}
