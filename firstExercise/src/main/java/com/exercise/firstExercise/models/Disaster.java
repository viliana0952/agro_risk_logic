package com.exercise.firstExercise.models;

public enum Disaster {
    GRADUSHKA,
    SILNI_VETROVE,
    PROLIVEN_DAJD_NAVODNENIE,
    OGAN,
    OSLANQVANE,
    KISHA,
    IZMRAZVANE,
    IZTEGLQNE;

    public static Disaster fromString(String str) {
        try {
            return Disaster.valueOf(str.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown disaster type: " + str);
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    }
