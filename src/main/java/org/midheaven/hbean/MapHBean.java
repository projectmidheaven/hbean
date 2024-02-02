package org.midheaven.hbean;

import org.midheaven.lang.reflection.Mirror;

import java.util.HashMap;
import java.util.Map;

public class MapHBean implements HBean {

    private record MapHField(MapHBean bean, HBeanDefinition.Field field) implements HField {
        @Override
        public boolean isDefined() {
            return true;
        }

        @Override
        public String name() {
            return field.name();
        }

        @Override
        public Object getValue() {
            return bean.valuesMapping.get(field.name());
        }

        @Override
        public void setValue(Object value) {
            if (value == null){
                bean.valuesMapping.remove(field.name());
            } else {
                bean.valuesMapping.put(field.name(), value);
            }
        }

        @Override
        public HBeanDefinition.Field definition() {
            return field;
        }
    }
    private record UnDefinedField(MapHBean bean, String name) implements HField {
        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object value) {
           bean.valuesMapping.put(name.toLowerCase(), value);
           bean.definition.add(name, value == null ? Object.class : value.getClass());
        }

        @SuppressWarnings("unchecked")
        @Override
        public HBeanDefinition.Field definition() {
            return new HBeanDefinition.Field() {
                @Override
                public String name() {
                    return name;
                }

                @Override
                public Class<?> type() {
                    return Void.class;
                }
            };
        }
    }

    private final HBeanDefinition definition;
    private final Map<String, Object> valuesMapping = new HashMap<>();

    MapHBean(HBeanDefinition definition){
        this.definition = definition;
    }

    MapHBean(HBeanDefinition definition, Map<String, Object> other){
        this.definition = definition;
        this.valuesMapping.putAll(other);
    }

    @Override
    public HBeanDefinition definition() {
        return definition;
    }

    @Override
    public HField hField(String fieldName) {
        return definition.field(fieldName)
                .<HField>map(fieldDefinition -> new MapHField(this, fieldDefinition))
                .orElseGet(() -> new UnDefinedField(this, fieldName));
    }

    @Override
    public <T> T as(Class<T> contract) {
        return Mirror.reflect(contract).proxy( new HBeanInvocationAdapter(this), HBean.class);
    }
}
