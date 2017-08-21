package com.mommoo.flat.layout.exception;

public class MismatchException extends Exception{
    public MismatchException() {
        super("Sum of component's weight isn't bigger than layout weightSum");
    }
}
