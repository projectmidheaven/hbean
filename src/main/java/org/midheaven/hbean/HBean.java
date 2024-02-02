package org.midheaven.hbean;

import org.midheaven.lang.reflection.Mirror;

import java.util.Map;

public interface HBean {

    static HBean newInstance(){
        return new MapHBean(new MapHBeanDefinition(), Map.of());
    }

    static HBean newInstance(HBeanDefinition definition){
        return new MapHBean(definition, Map.of());
    }

    static HBean newInstance(Class<?> type){
        return (HBean)Mirror.reflect(type).proxy( new HBeanInvocationAdapter(newInstance(HBeanDefinition.from(type))), HBean.class);
    }

    static HBean from(Map<String, Object> data){
        // create compatible HBeanDefinition
        var definition = new MapHBeanDefinition();

        for (var entry : data.entrySet()){
            definition.add(
                    entry.getKey().toLowerCase(),
                    entry.getValue() == null
                            ? Object.class
                            : entry.getValue().getClass()
            );
        }
        return from(data, definition);
    }

    static HBean from(Map<String, Object> data, HBeanDefinition definition){
        return new MapHBean(definition, data);
    }

    HBeanDefinition definition();

    HField hField(String fieldName);

    <T> T as(Class<T> contract);
}
