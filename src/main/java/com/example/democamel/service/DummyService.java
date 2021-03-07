package com.example.democamel.service;

import com.example.democamel.model.Dummy;

import java.util.Collection;

public interface DummyService {
    Dummy findUser(Integer id);
    Collection<Dummy> findUsers();
    void updateUser(Dummy dummy);
    Dummy create(Dummy dummy);
    void delete(Integer id);
    long count();
}
