package io.github.projectunified.unidialog.adventure.support;

import net.kyori.adventure.text.Component;

import java.util.function.Function;

/**
 * An interface that provides support for handling {@link Component} serialization and deserialization.
 * It allows for the conversion of strings to {@link Component} objects using a provided deserializer function.
 */
public interface AdventureSupport {
    /**
     * Get the deserializer function that converts a string to a {@link Component}
     *
     * @return the function that deserializes a string into a Component
     */
    Function<String, Component> getComponentDeserializer();

    /**
     * Applies the deserializer to the given input string to convert it into a {@link Component}
     *
     * @param input the input string to be deserialized
     * @return the deserialized {@link Component}
     */
    default Component deserialize(String input) {
        return getComponentDeserializer().apply(input);
    }
}
