package org.midheaven.hbean;

import org.midheaven.lang.reflection.Mirror;

import java.util.Optional;

public interface HBeanDefinition {

    static HBeanDefinition from(Class<?> type){
        var definition = new MapHBeanDefinition();
        for (var p : Mirror.reflect(type).properties()){
            definition.add(p.name().toLowerCase(), p.valueType());
        }
        return definition;
    }

    Optional<Field> field(String name);
    HBeanDefinition add(String name, Class<?> type);

    interface Field {
        String name();
        Class<?> type();
    }



}
