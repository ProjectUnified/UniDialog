package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBody;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInput;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import io.github.projectunified.unidialog.viaversion.opener.ViaDialogOpener;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link Dialog} for building dialogs.
 *
 * @param <T> the type of the dialog itself, for method chaining
 */
@SuppressWarnings("unchecked")
public abstract class ViaDialog<T extends ViaDialog<T>> implements Dialog<ViaItem, ViaDialogBodyBuilder, ViaDialogInputBuilder, T> {
    private final String defaultNamespace;
    private final Function<String, TextComponent> componentDeserializer;

    private TextComponent title;
    private @Nullable TextComponent externalTitle;
    private boolean canCloseWithEscape = true;
    private boolean pause = false;
    private AfterAction afterAction = AfterAction.CLOSE;
    private List<ViaDialogBody> bodies;
    private List<InputEntry> inputs;

    /**
     * Constructor for ViaDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     */
    protected ViaDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    private static String toAfterAction(AfterAction afterAction) {
        return switch (afterAction) {
            case CLOSE -> "close";
            case WAIT_FOR_RESPONSE -> "wait_for_response";
            case NONE -> "none";
        };
    }

    /**
     * Deserialize a string to a {@link TextComponent}
     *
     * @param input the string to deserialize
     * @return the deserialized component
     */
    protected TextComponent deserialize(String input) {
        return componentDeserializer.apply(input);
    }

    /**
     * Set the title of the dialog
     *
     * @param title the title
     * @return the dialog itself for method chaining
     */
    public T title(TextComponent title) {
        this.title = title;
        return (T) this;
    }

    @Override
    public T title(String title) {
        return title(deserialize(title));
    }

    /**
     * Set the external title of the dialog, used for displaying the dialog in a multi-dialog dialog
     *
     * @param externalTitle the external title
     * @return the dialog itself for method chaining
     */
    public T externalTitle(@Nullable TextComponent externalTitle) {
        this.externalTitle = externalTitle;
        return (T) this;
    }

    @Override
    public T externalTitle(@Nullable String externalTitle) {
        return externalTitle(externalTitle == null ? null : deserialize(externalTitle));
    }

    @Override
    public T canCloseWithEscape(boolean canCloseWithEscape) {
        this.canCloseWithEscape = canCloseWithEscape;
        return (T) this;
    }

    @Override
    public T pause(boolean pause) {
        this.pause = pause;
        return (T) this;
    }

    @Override
    public T afterAction(AfterAction afterAction) {
        this.afterAction = afterAction;
        return (T) this;
    }

    @Override
    public T body(Consumer<ViaDialogBodyBuilder> bodyBuilder) {
        if (bodies == null) {
            bodies = new ArrayList<>();
        }
        ViaDialogBodyBuilder builder = new ViaDialogBodyBuilder(componentDeserializer);
        bodyBuilder.accept(builder);
        bodies.add(builder.getDialogBody());
        return (T) this;
    }

    @Override
    public T input(String key, Consumer<ViaDialogInputBuilder> inputBuilder) {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        ViaDialogInputBuilder builder = new ViaDialogInputBuilder(componentDeserializer);
        inputBuilder.accept(builder);
        inputs.add(new InputEntry(key, builder.getInput()));
        return (T) this;
    }

    /**
     * Create a dialog action builder configured by the given consumer
     *
     * @param action the action builder consumer
     * @return the configured action builder
     */
    protected ViaDialogActionBuilder getActionBuilder(Consumer<ViaDialogActionBuilder> action) {
        ViaDialogActionBuilder actionBuilder = new ViaDialogActionBuilder(defaultNamespace, componentDeserializer);
        action.accept(actionBuilder);
        return actionBuilder;
    }

    /**
     * Write the type-specific fields of this dialog, including the {@code type} key
     *
     * @param tag           the tag to write into
     * @param baseVersion   the serializer of the server version the dialog is created for
     * @param targetVersion the serializer of the version the dialog is sent to
     */
    protected abstract void writeDialogType(CompoundTag tag, SerializerVersion baseVersion, SerializerVersion targetVersion);

    /**
     * Build the dialog {@link CompoundTag} for the given base and target protocol versions
     *
     * @param baseVersion   the protocol version of the server the dialog is created for
     * @param targetVersion the protocol version of the player the dialog is sent to
     * @return the dialog tag
     */
    public final CompoundTag getDialogTag(ProtocolVersion baseVersion, ProtocolVersion targetVersion) {
        return getDialogTag(ViaDialogTagBuilder.serializerFor(baseVersion), ViaDialogTagBuilder.serializerFor(targetVersion));
    }

    /**
     * Build the dialog {@link CompoundTag} for the given base and target serializers
     *
     * @param baseVersion   the serializer of the server version the dialog is created for
     * @param targetVersion the serializer of the version the dialog is sent to
     * @return the dialog tag
     */
    public final CompoundTag getDialogTag(SerializerVersion baseVersion, SerializerVersion targetVersion) {
        CompoundTag tag = new CompoundTag();
        ViaDialogTagBuilder.putComponent(tag, "title", title != null ? title : new StringComponent("Dialog"), baseVersion, targetVersion);
        ViaDialogTagBuilder.putComponent(tag, "external_title", externalTitle, baseVersion, targetVersion);
        tag.putBoolean("can_close_with_escape", canCloseWithEscape);
        tag.putBoolean("pause", pause);
        tag.putString("after_action", toAfterAction(afterAction));
        if (bodies != null && !bodies.isEmpty()) {
            ListTag<CompoundTag> bodyTag = new ListTag<>(CompoundTag.class);
            for (ViaDialogBody body : bodies) {
                bodyTag.add(body.toTag(baseVersion, targetVersion));
            }
            tag.put("body", bodyTag);
        }
        if (inputs != null && !inputs.isEmpty()) {
            ListTag<CompoundTag> inputsTag = new ListTag<>(CompoundTag.class);
            for (InputEntry entry : inputs) {
                CompoundTag inputTag = new CompoundTag();
                inputTag.putString("key", entry.key);
                entry.input.write(inputTag, baseVersion, targetVersion);
                inputsTag.add(inputTag);
            }
            tag.put("inputs", inputsTag);
        }
        writeDialogType(tag, baseVersion, targetVersion);
        return tag;
    }

    @Override
    public ViaDialogOpener opener() {
        return new ViaDialogOpener(this);
    }

    private record InputEntry(String key, ViaDialogInput input) {
    }
}
