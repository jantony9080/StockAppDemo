package ru.interview.api.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

public class JacksonReader<T> implements DataReader<T>{

    public final Class<T> type;
    private String pathToJson;

    public JacksonReader(Class<T> type, String pathToJson) {
        this.type = type;
        this.pathToJson=pathToJson;
    }


    @Override
    public Collection<T> readData() {
        ObjectMapper mapper = new ObjectMapper();
        T[] objects = null;
        try {
            InputStream is = new ClassPathResource(pathToJson).getInputStream();
            Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + type.getName() + ";");
            objects = mapper.readValue(is, arrayClass);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Arrays.asList(objects);
    }
}
