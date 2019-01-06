package io.codeleaf.logging.example;

import io.codeleaf.logging.core.LoggingBindings;

public final class Example {

    public static void main(String[] args) {
        LoggingBindings.init();
        System.out.println("Hello INFO!");
        System.err.println("Hello ERROR!");
    }

}
