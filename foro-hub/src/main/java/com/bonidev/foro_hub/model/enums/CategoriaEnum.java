package com.bonidev.foro_hub.model.enums;

public enum CategoriaEnum {
    PROGRAMACION,
    MATEMATICAS,
    FISICA,
    QUIMICA,
    BIOLOGIA,
    HISTORIA,
    GEOGRAFIA,
    FILOSOFIA;

    public String capitalize() {
        var word = this.toString().toLowerCase();
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
}
