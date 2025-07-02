package io.github.projectunified.unidialog.core.body;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ItemBody<I, B extends TextBody<B>, T extends ItemBody<I, B, T>> {
    T item(I item);

    T description(@Nullable Consumer<B> descriptionBuilder);

    T showDecorations(boolean showDecorations);

    T showTooltip(boolean showTooltip);

    T width(int width);

    T height(int height);
}
