package com.telus.dsu.libraryapi.exception;

public class ResourceNotCreatedException extends RuntimeException{
    private String message;

    public ResourceNotCreatedException(String message) {
        super(message);
        this.message = message;
    }
}
