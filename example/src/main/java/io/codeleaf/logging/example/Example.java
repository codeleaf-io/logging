package io.codeleaf.logging.example;

import io.codeleaf.logging.core.LogBindings;

public final class Example {

    public static void main(String[] args) {
        LogBindings.init();
        System.out.println("Hello INFO!");
        System.err.println("Hello ERROR!");
    }

}
