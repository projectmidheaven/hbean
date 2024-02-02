package org.midheaven.hbean;

import org.midheaven.lang.reflection.InvocationHandler;
import org.midheaven.lang.reflection.Mirror;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class HBeanInvocationAdapter implements InvocationHandler {

    private record MethodInfo(String propertyName, boolean isModifier){}

    private final HBean hBean;
    private Map<String, MethodInfo> methodNamesPropertiesMapping = new HashMap<>();

    public HBeanInvocationAdapter(HBean hBean) {
        this.hBean = hBean;
    }

    @Override
    public Object handleInvocation(Object object, Method method, Object[] objects) throws Throwable {
        if (method.isDefault()) {
            // if it's a default method, invoke it
            return java.lang.reflect.InvocationHandler.invokeDefault(object, method, objects);
        } else if ("hField".equals(method.getName()) && objects.length == 1){
            return hBean.hField(String.valueOf(objects[0]));
        } else if ("as".equals(method.getName())){
            return object;
        } else if ("definition".equals(method.getName())){
            return hBean.definition();
        }

        // real bean calls
        var info = methodNamesPropertiesMapping.computeIfAbsent(method.getName(), name -> {
            if (objects!= null && objects.length == 1){
                return Mirror.reflect(method).map(p -> new MethodInfo(p.name(), true))
                        .orElse(null);
            } else if (objects == null || objects.length == 0){
                return Mirror.reflect(method).map(p -> new MethodInfo(p.name(), false))
                        .orElse(null);
            }
            return null;
        });

        if (info == null){
            throw new IllegalArgumentException("Cannot call HBean method " + method);
        } else if (info.isModifier && objects != null && objects.length  == 1){
            hBean.hField(info.propertyName).setValue(objects[0]);
            return null;
        }

        return hBean.hField(info.propertyName).getValue();
    }
}
