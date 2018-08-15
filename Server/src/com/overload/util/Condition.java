package com.overload.util;

@FunctionalInterface
public interface Condition<T> {

    boolean test(T t);

}
