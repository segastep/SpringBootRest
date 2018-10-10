package com.restapi.demo.services;

import java.util.List;

public interface CRUDService<T> {

    List<?> listAll();

    T getById(Integer id);
    T saveOrUpdate(T domainObj);

    void delete(Integer id);

}
