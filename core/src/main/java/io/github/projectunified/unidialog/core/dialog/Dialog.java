package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface Dialog<I, T extends Dialog<I, T>> {
    T title(String title);

    T externalTitle(String externalTitle);

    T canCloseWithEscape(boolean canCloseWithEscape);

    T pause(boolean pause);

    T afterAction(AfterAction afterAction);

    <B extends DialogBodyBuilder<I>> T body(Consumer<B> bodyBuilder);

    <B extends DialogBodyBuilder<I>> T body(Collection<Consumer<B>> bodyBuilders);

    <B extends DialogInputBuilder> T input(String key, Consumer<B> inputBuilder);

    <B extends DialogInputBuilder> T input(Map<String, Consumer<B>> inputBuilders);

    boolean open(UUID uuid);

    enum AfterAction {
        CLOSE,
        NONE,
        WAIT_FOR_RESPONSE
    }
}
