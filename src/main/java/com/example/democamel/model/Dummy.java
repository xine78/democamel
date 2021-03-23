package com.example.democamel.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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

}
