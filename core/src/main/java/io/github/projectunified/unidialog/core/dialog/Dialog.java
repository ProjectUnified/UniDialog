package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public interface Dialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, T extends Dialog<I, BB, IB, T>> {
    T title(String title);

    T externalTitle(@Nullable String externalTitle);

    T canCloseWithEscape(boolean canCloseWithEscape);

    T pause(boolean pause);

    T afterAction(AfterAction afterAction);

    T body(Consumer<BB> bodyBuilder);

    T body(Collection<Consumer<BB>> bodyBuilders);

    T input(String key, Consumer<IB> inputBuilder);

    T input(Map<String, Consumer<IB>> inputBuilders);

    DialogOpener opener();

    enum AfterAction {
        CLOSE,
        NONE,
        WAIT_FOR_RESPONSE
    }
}
