package com.example.democamel.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Dummy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Dummy() {
    }

    public Dummy(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dummy)) return false;
        Dummy dummy = (Dummy) o;
        return getId().equals(dummy.getId()) && name.equals(dummy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
