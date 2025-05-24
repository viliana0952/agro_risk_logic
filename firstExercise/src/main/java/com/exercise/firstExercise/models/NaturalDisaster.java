package com.exercise.firstExercise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "naturalDisasters")
@Data
public class NaturalDisaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private Disaster naturalDisaster;

    @OneToMany(mappedBy="natural_disaster")
    private List<Question> questions;
}
