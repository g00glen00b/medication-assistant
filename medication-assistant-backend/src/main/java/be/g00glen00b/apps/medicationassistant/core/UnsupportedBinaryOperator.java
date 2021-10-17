package be.g00glen00b.apps.medicationassistant.core;

import java.util.function.BinaryOperator;

public class UnsupportedBinaryOperator<T> implements BinaryOperator<T> {
    @Override
    public T apply(T t, T t2) {
        throw new UnsupportedOperationException("Parallel streaming of this operation is not supported");
    }
}
