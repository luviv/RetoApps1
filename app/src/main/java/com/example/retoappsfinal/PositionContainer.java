package com.example.retoappsfinal;

import androidx.annotation.NonNull;

public class PositionContainer {

    private Position location;

    public PositionContainer() {

    }

    @NonNull
    @Override
    public String toString() {
        return location.toString();
    }

    public PositionContainer(Position pos) {
        this.location = pos;
    }

    public Position getPos() {
        return location;
    }

    public void setPos(Position pos) {
        this.location = pos;
    }
}
