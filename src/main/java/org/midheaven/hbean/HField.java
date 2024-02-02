package org.midheaven.hbean;

public interface HField {

    boolean isDefined();
    String name();
    Object getValue();
    void setValue(Object value);

    HBeanDefinition.Field definition();

}
