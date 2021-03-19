package com.example.democamel.dao;

import com.example.democamel.model.Dummy;
import org.apache.camel.spi.annotations.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyDao extends JpaRepository<Dummy, Integer> {
}
