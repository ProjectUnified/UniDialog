package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface Dialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, T extends Dialog<I, BB, IB, T>> {
    T title(String title);

    T externalTitle(String externalTitle);

    T canCloseWithEscape(boolean canCloseWithEscape);

    T pause(boolean pause);

    T afterAction(AfterAction afterAction);

    T body(Consumer<BB> bodyBuilder);

    T body(Collection<Consumer<BB>> bodyBuilders);

    T input(String key, Consumer<IB> inputBuilder);

    T input(Map<String, Consumer<IB>> inputBuilders);

    boolean open(UUID uuid);

    enum AfterAction {
        CLOSE,
        NONE,
        WAIT_FOR_RESPONSE
    }
}
