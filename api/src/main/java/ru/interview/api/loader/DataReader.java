package ru.interview.api.loader;

import java.util.Collection;

public interface DataReader<T> {
    public Collection<T> readData();
}
