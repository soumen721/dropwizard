package com.example.dw.jpa.dao;

import java.util.List;
import java.util.Map;

public interface Dao {

    <T> void persist(T object);

    <T, ID> T findById(Class<T> clazz, ID id);

    <T> T merge(T object);

    <T> void remove(T object);

    <T, ID> void removeById(Class<T> clazz, ID id);
    
    <T, ID> void removeAll(Class<T> clazz);

    /*<T> List<T> findAll(Class clazz);*/

    <T> List<T> find(Class<T> clazz, String namedQuery, Map<String, Object> paramsMap);

	<T> List<T> findAll(Class clazz, String orderBy);

}
