package br.edu.ufersa.server.gateway.domain;

import java.io.Serializable;

public enum CarCategory implements Serializable {
    ECONOMY,
    INTERMEDIARY,
    EXCLUSIVE;

    public static CarCategory fromNumber(int number) {
        int mod = number%3;
        return switch (mod) {
            case 1 -> INTERMEDIARY;
            case 2 -> EXCLUSIVE;
            default -> ECONOMY;
        };
    }
}
