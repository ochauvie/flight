package com.flightbook.tools;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by o.chauvie on 17/02/2015.
 */
public class MyExclusionStrategy implements ExclusionStrategy {
    private final Class<?> typeToSkip;

    public MyExclusionStrategy(Class<?> typeToSkip) {
        this.typeToSkip = typeToSkip;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return (clazz == typeToSkip);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(JsonExclude.class) != null;
    }
}
