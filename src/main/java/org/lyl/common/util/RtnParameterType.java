package org.lyl.common.util;

import org.springframework.core.ParameterizedTypeReference;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;

public class RtnParameterType<T> extends ParameterizedTypeReference<T> {

    private Type type;

    public RtnParameterType(Class<?> originClass, Class<?>... genericClass) {
        Type type = ParameterizedTypeImpl.make(originClass, genericClass, null);
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

}
