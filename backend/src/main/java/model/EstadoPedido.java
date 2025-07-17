package model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoPedido {
    PENDIENTE,
    TOMADO,
    COMPLETADO;


    @JsonCreator
    public static EstadoPedido fromString(String value) {
        return EstadoPedido.valueOf(value.toUpperCase());
    }

}


