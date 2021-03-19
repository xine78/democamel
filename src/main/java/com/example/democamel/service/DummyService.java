package com.example.democamel.service;

import com.example.democamel.model.Dummy;

import java.util.Collection;

public interface DummyService {
    Dummy findDummy(Integer id);
    Collection<Dummy> findDummys();
    void updateDummy(Integer id, Dummy dummy);
    Dummy create(Dummy dummy);
    void delete(Integer id);
    long count();
}
