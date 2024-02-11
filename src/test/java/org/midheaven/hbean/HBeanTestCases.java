package org.midheaven.hbean;

import org.junit.jupiter.api.Test;
import org.midheaven.hbean.model.Client;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HBeanTestCases {

    @Test
    public void unboundHBean() {
        var bean = HBean.newInstance();

        assertFalse(bean.hField("name").isDefined());

        bean.hField("name").setValue("John");

        assertTrue(bean.hField("name").isDefined());

        assertEquals("John", bean.hField("name").getValue());

    }

    @Test
    public void boundHBean(){
        var bean = HBean.newInstance(Client.class);

        assertTrue(bean instanceof Client, "HBean does not implement definition type");

        bean.hField("name").setValue("John");
        bean.hField("age").setValue(23);

        assertEquals("John", bean.hField("name").getValue());
        assertEquals(23, bean.hField("age").getValue());
        assertEquals(null, bean.hField("non-existing-field").getValue());

        assertEquals("age", bean.hField("age").definition().name());
        assertEquals("non-existing-field", bean.hField("non-existing-field").name());

        var client = bean.as(Client.class);

        assertEquals(23, client.getAge());

        client.setAge(42);

        assertEquals(42, bean.hField("age").getValue());
        assertEquals(42, client.getAge());

        assertEquals(3, client.sum(1,2));
    }

    @Test
    public void jsonLikeMapHBean() {
        Map<String, Object> json = Map.of("name", "John", "age", 23);
        var bean = HBean.from(json);

        assertEquals("John", bean.hField("name").getValue());
        assertEquals(23, bean.hField("age").getValue());
        assertEquals(null, bean.hField("non-existing-field").getValue());


    }

}
