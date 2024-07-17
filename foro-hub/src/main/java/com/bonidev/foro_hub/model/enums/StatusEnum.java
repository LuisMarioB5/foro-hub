package com.bonidev.foro_hub.model.enums;

public enum StatusEnum {
    ABIERTO,
    CERRADO,
    EN_PROGRESO,
    RESUELTO;

    public String capitalize() {
        var word = this.toString().toLowerCase();
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
}
