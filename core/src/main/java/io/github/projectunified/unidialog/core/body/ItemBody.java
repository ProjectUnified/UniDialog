package io.github.projectunified.unidialog.core.body;

import java.util.function.Consumer;

public interface ItemBody<I, T extends ItemBody<I, T>> {
    T item(I item);

    <B extends TextBody<B>> T description(Consumer<B> descriptionBuilder);

    T showDecorations(boolean showDecorations);

    T showTooltip(boolean showTooltip);

    T width(int width);

    T height(int height);
}
