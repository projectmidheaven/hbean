package org.midheaven.hbean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapHBeanDefinition implements HBeanDefinition
{
    private record MapHField<T>(String name, Class<T> type) implements Field{ }

    private final Map<String, Field> fieldsMapping = new HashMap<>();

    @Override
    public Optional<Field> field(String name) {
        return Optional.ofNullable(fieldsMapping.get(name.toLowerCase()));
    }

    @Override
    public HBeanDefinition add(String name, Class<?> type){
        fieldsMapping.put(name.toLowerCase(), new MapHField<>(name, type));
        return this;
    }
}
