package com.exercise.firstExercise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name="disaster_type", nullable = false,unique = true)
    private Disaster disaster;

    @Column(name="options",nullable = false)
    private int options;

//    @ManyToOne
//    @JoinColumn(name = "natural_disaster")
//    private NaturalDisaster naturalDisaster;
}
