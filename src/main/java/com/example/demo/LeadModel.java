package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lead")
@Getter
@Setter
public class LeadModel {

    @Id
    @GeneratedValue(generator = "lead_generator")
    @SequenceGenerator(
            name = "lead_generator",
            sequenceName = "lead_sequence",
            initialValue = 1
    )
    private Long id;

    @Column
    private String number;

    @Column
    private String name;
}
