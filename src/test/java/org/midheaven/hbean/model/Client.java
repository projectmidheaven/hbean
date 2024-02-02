package org.midheaven.hbean.model;

public interface Client {

    String getName();
    void setName(String name);

    Integer getAge();
    void setAge(Integer age);

    default int sum(int a, int b){
        return a + b;
    }
}
