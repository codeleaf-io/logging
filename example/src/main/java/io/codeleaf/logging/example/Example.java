package io.codeleaf.logging.example;

import io.codeleaf.logging.core.LogBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Example {

    public static void main(String[] args) {
        LogBindings.init();
        System.out.println("Hello INFO!");
        System.err.println("Hello ERROR!");

        Logger logger = LoggerFactory.getLogger("eventLog");
        logger.warn("This is a WARN!");
        logger.debug("And some DEBUG!");
    }

}
