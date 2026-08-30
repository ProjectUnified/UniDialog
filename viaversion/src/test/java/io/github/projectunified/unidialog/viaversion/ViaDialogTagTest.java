package io.github.projectunified.unidialog.viaversion;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataKey;
import com.viaversion.viaversion.api.minecraft.data.StructuredData;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataContainer;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.viaversion.dialog.ViaConfirmationDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaDialogListDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaMultiActionDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaNoticeDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaServerLinksDialog;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the dialog NBT shape produced by the Via builders against the vanilla
 * {@code minecraft:dialog} structure consumed by {@code RegistryDataRewriter#updateDialog}
 * and ViaBackwards' {@code Dialog} parser.
 */
class ViaDialogTagTest {

    private static final class TestManager extends ViaVersionDialogManager {
        TestManager() {
            super("test");
        }
    }
    /**
     * Extracts the plain text of a text component tag, which may be either a plain string
     * (the vanilla NBT shorthand) or a compound with a {@code text} key.
     */
    private static String textOf(Tag tag) {
        if (tag instanceof StringTag stringTag) {
            return stringTag.getValue();
        }
        return ((CompoundTag) tag).getString("text");
    }

    @Test
    void confirmationDialog() {
        ViaConfirmationDialog dialog = new TestManager().createConfirmationDialog();
        dialog.title("Hello").pause(true).canCloseWithEscape(false);
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        assertEquals("minecraft:confirmation", tag.getString("type"));
        assertEquals("Hello", textOf(tag.get("title")));
        assertFalse(tag.getBoolean("can_close_with_escape"));
        assertTrue(tag.getBoolean("pause"));
        assertEquals("close", tag.getString("after_action"));

        CompoundTag yes = tag.getCompoundTag("yes");
        assertNotNull(yes, "yes button must be present");
        assertEquals("gui.yes", ((CompoundTag) yes.get("label")).getString("translate"));
        assertEquals(150, yes.getInt("width"));
        assertNotNull(tag.getCompoundTag("no"), "no button must be present");
    }

    @Test
    void confirmationDialogCustomActions() {
        ViaConfirmationDialog dialog = new TestManager().createConfirmationDialog();
        dialog.yesAction(action -> action.label("Accept").runCommand("accept"));
        dialog.noAction(action -> action.label("Deny").copyToClipboard("denied"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        CompoundTag yes = tag.getCompoundTag("yes");
        assertEquals("Accept", textOf(yes.get("label")));
        assertEquals("minecraft:run_command", yes.getCompoundTag("action").getString("type"));
        assertEquals("accept", yes.getCompoundTag("action").getString("command"));

        CompoundTag no = tag.getCompoundTag("no");
        assertEquals("minecraft:copy_to_clipboard", no.getCompoundTag("action").getString("type"));
        assertEquals("denied", no.getCompoundTag("action").getString("value"));
    }

    @Test
    void multiActionDialog() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.columns(3)
            .action(action -> action.label("One").dynamicCustom("custom_action"))
            .action(action -> action.label("Two").openUrl("https://example.com"))
            .exitAction(action -> action.label("Exit").suggestCommand("exit"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        assertEquals("minecraft:multi_action", tag.getString("type"));
        assertEquals(3, tag.getInt("columns"));

        ListTag<CompoundTag> actions = tag.getListTag("actions", CompoundTag.class);
        assertNotNull(actions);
        assertEquals(2, actions.size());
        CompoundTag first = actions.get(0);
        assertEquals("minecraft:dynamic/custom", first.getCompoundTag("action").getString("type"));
        assertEquals("test:custom_action", first.getCompoundTag("action").getString("id"));
        assertNotNull(first.getCompoundTag("action").getCompoundTag("additions"));
        assertEquals("https://example.com", actions.get(1).getCompoundTag("action").getString("url"));

        CompoundTag exit = tag.getCompoundTag("exit_action");
        assertNotNull(exit);
        assertEquals("minecraft:suggest_command", exit.getCompoundTag("action").getString("type"));
    }

    @Test
    void noticeDialog() {
        ViaNoticeDialog dialog = new TestManager().createNoticeDialog();
        dialog.action(action -> action.label("OK").runCommand("ok"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        assertEquals("minecraft:notice", tag.getString("type"));
        CompoundTag action = tag.getCompoundTag("action");
        assertNotNull(action);
        assertEquals("minecraft:run_command", action.getCompoundTag("action").getString("type"));
        assertEquals("ok", action.getCompoundTag("action").getString("command"));
    }

    @Test
    void serverLinksDialog() {
        ViaServerLinksDialog dialog = new TestManager().createServerLinksDialog();
        dialog.columns(4).buttonWidth(120);
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        assertEquals("minecraft:server_links", tag.getString("type"));
        assertEquals(4, tag.getInt("columns"));
        assertEquals(120, tag.getInt("button_width"));
        assertFalse(tag.contains("exit_action"));
    }

    @Test
    void dialogListDialogInlined() {
        ViaDialogListDialog dialog = new TestManager().createDialogListDialog();
        ViaConfirmationDialog child = new TestManager().createConfirmationDialog();
        child.title("Child");
        dialog.dialog(child).exitAction(action -> action.label("Back"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        assertEquals("minecraft:dialog_list", tag.getString("type"));
        ListTag<CompoundTag> dialogs = tag.getListTag("dialogs", CompoundTag.class);
        assertNotNull(dialogs);
        assertEquals(1, dialogs.size());
        assertEquals("minecraft:confirmation", dialogs.get(0).getString("type"));
        assertNotNull(tag.getCompoundTag("exit_action"));
    }

    @Test
    void dialogListDialogRegistryReferences() {
        ViaDialogListDialog dialog = new TestManager().createDialogListDialog();
        dialog.dialog("minecraft", "some_dialog").dialog("myplugin", "other_dialog");
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        ListTag<StringTag> dialogs = tag.getListTag("dialogs", StringTag.class);
        assertNotNull(dialogs);
        assertEquals(2, dialogs.size());
        assertEquals("minecraft:some_dialog", dialogs.get(0).getValue());
        assertEquals("myplugin:other_dialog", dialogs.get(1).getValue());
    }

    @Test
    void dialogListDialogCannotMix() {
        ViaDialogListDialog dialog = new TestManager().createDialogListDialog();
        dialog.dialog(new TestManager().createNoticeDialog()).dialog("minecraft", "some_dialog");
        assertThrows(IllegalArgumentException.class, () -> dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6));
    }

    @Test
    void bodies() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.body(builder -> builder.text().text("Hello world").width(120));
        dialog.body(builder -> builder.item().item(new com.viaversion.viaversion.api.minecraft.item.DataItem(24, (byte) 2, null))
            .description(text -> text.text("An item").width(90))
            .showTooltip(false)
            .width(32)
            .height(32));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        ListTag<CompoundTag> bodies = tag.getListTag("body", CompoundTag.class);
        assertNotNull(bodies);
        assertEquals(2, bodies.size());

        CompoundTag text = bodies.get(0);
        assertEquals("minecraft:plain_message", text.getString("type"));
        assertEquals("Hello world", textOf(text.get("contents")));
        assertEquals(120, text.getInt("width"));

        CompoundTag item = bodies.get(1);
        assertEquals("minecraft:item", item.getString("type"));
        CompoundTag itemTag = item.getCompoundTag("item");
        assertNotNull(itemTag);
        assertNotNull(itemTag.getString("id"));
        assertEquals(2, itemTag.getInt("count"));
        assertEquals("An item", textOf(item.getCompoundTag("description").get("contents")));
        assertFalse(item.getBoolean("show_tooltip"));
        assertTrue(item.getBoolean("show_decorations"));
        assertEquals(32, item.getInt("width"));
        assertEquals(32, item.getInt("height"));
    }

    @Test
    void inputs() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.input("flag", builder -> builder.booleanInput().label("Toggle").initial(true).onTrue("yes").onFalse("no"));
        dialog.input("name", builder -> builder.textInput().label("Name").initial("Steve").maxLength(16));
        dialog.input("choice", builder -> builder.singleOptionInput().label("Pick").option("a", "A", true).option("b", "B"));
        dialog.input("amount", builder -> builder.numberRangeInput().label("Amount").start(0).end(10).initial(5f).step(1f));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        ListTag<CompoundTag> inputs = tag.getListTag("inputs", CompoundTag.class);
        assertNotNull(inputs);
        assertEquals(4, inputs.size());

        CompoundTag booleanInput = inputs.get(0);
        assertEquals("minecraft:boolean", booleanInput.getString("type"));
        assertEquals("flag", booleanInput.getString("key"));
        assertEquals("Toggle", textOf(booleanInput.get("label")));
        assertTrue(booleanInput.getBoolean("initial"));
        assertEquals("yes", booleanInput.getString("on_true"));
        assertEquals("no", booleanInput.getString("on_false"));

        CompoundTag textInput = inputs.get(1);
        assertEquals("minecraft:text", textInput.getString("type"));
        assertEquals("name", textInput.getString("key"));
        assertEquals("Name", textOf(textInput.get("label")));
        assertTrue(textInput.getBoolean("label_visible"));
        assertEquals("Steve", textInput.getString("initial"));
        assertEquals(16, textInput.getInt("max_length"));
        assertEquals(200, textInput.getInt("width"));

        CompoundTag singleOption = inputs.get(2);
        assertEquals("minecraft:single_option", singleOption.getString("type"));
        ListTag<CompoundTag> options = singleOption.getListTag("options", CompoundTag.class);
        assertNotNull(options);
        assertEquals(2, options.size());
        assertEquals("a", options.get(0).getString("id"));
        assertTrue(options.get(0).getBoolean("initial"));
        assertFalse(options.get(1).getBoolean("initial"));

        CompoundTag numberRange = inputs.get(3);
        assertEquals("minecraft:number_range", numberRange.getString("type"));
        assertEquals(0, numberRange.getFloat("start"), 0);
        assertEquals(10, numberRange.getFloat("end"), 0);
        assertEquals(5, numberRange.getFloat("initial"), 0);
        assertEquals(1, numberRange.getFloat("step"), 0);
        assertEquals("options.generic_value", numberRange.getString("label_format"));
    }

    @Test
    void textInputMultiline() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.input("notes", builder -> builder.textInput().label("Notes").maxLines(3).height(4));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);
        CompoundTag multiline = tag.getListTag("inputs", CompoundTag.class).get(0).getCompoundTag("multiline");
        assertNotNull(multiline);
        assertEquals(3, multiline.getInt("max_lines"));
        assertEquals(4, multiline.getInt("height"));
    }

    @Test
    void showDialogActionInlinesDialog() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        ViaNoticeDialog child = new TestManager().createNoticeDialog();
        child.title("Target");
        dialog.action(action -> action.label("Open").showDialog(child));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        CompoundTag action = tag.getListTag("actions", CompoundTag.class).get(0).getCompoundTag("action");
        assertEquals("minecraft:show_dialog", action.getString("type"));
        assertEquals("minecraft:notice", action.getCompoundTag("dialog").getString("type"));
    }

    @Test
    void showDialogRegistryReference() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.action(action -> action.label("Open").showDialog("minecraft", "dialog_id"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);

        CompoundTag action = tag.getListTag("actions", CompoundTag.class).get(0).getCompoundTag("action");
        assertEquals("minecraft:show_dialog", action.getString("type"));
        assertEquals("minecraft:dialog_id", action.getString("dialog"));
    }

    @Test
    void dynamicRunCommandTemplate() {
        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.action(action -> action.label("Run").dynamicRunCommand("tp @p {x}"));
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);
        CompoundTag action = tag.getListTag("actions", CompoundTag.class).get(0).getCompoundTag("action");
        assertEquals("minecraft:dynamic/run_command", action.getString("type"));
        assertEquals("tp @p {x}", action.getString("template"));
    }

    @Test
    void noBodiesAndNoInputsAreOmitted() {
        ViaNoticeDialog dialog = new TestManager().createNoticeDialog();
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);
        assertFalse(tag.contains("body"));
        assertFalse(tag.contains("inputs"));
    }

    @Test
    void afterActionValues() {
        ViaNoticeDialog close = new TestManager().createNoticeDialog();
        assertEquals("close", close.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6).getString("after_action"));

        ViaNoticeDialog waitForResponse = new TestManager().createNoticeDialog();
        waitForResponse.afterAction(ViaDialog.AfterAction.WAIT_FOR_RESPONSE);
        assertEquals("wait_for_response", waitForResponse.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6).getString("after_action"));

        ViaNoticeDialog none = new TestManager().createNoticeDialog();
        none.afterAction(ViaDialog.AfterAction.NONE);
        assertEquals("none", none.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6).getString("after_action"));
    }

    @Test
    void externalTitleWrittenWhenSet() {
        ViaNoticeDialog dialog = new TestManager().createNoticeDialog();
        dialog.externalTitle("External");
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6);
        assertEquals("External", textOf(tag.get("external_title")));
    }
    @Test
    void serializerForMapping() {
        assertEquals(SerializerVersion.V1_21_6, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21_6));
        assertEquals(SerializerVersion.V1_21_6, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v26_2));
        assertEquals(SerializerVersion.V1_21_4, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21_5));
        assertEquals(SerializerVersion.V1_21_4, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21_4));
        assertEquals(SerializerVersion.V1_20_5, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21));
        assertEquals(SerializerVersion.V1_20_5, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21_2));
        assertEquals(SerializerVersion.V1_21_4, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_21_4));
        assertEquals(SerializerVersion.V1_20_3, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_20_3));
        assertEquals(SerializerVersion.V1_20_3, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_20_3));
        assertEquals(SerializerVersion.V1_19_4, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_19_4));
        assertEquals(SerializerVersion.V1_18, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_19_3));
        assertEquals(SerializerVersion.V1_18, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_18));
        assertEquals(SerializerVersion.V1_17, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_17));
        assertEquals(SerializerVersion.V1_16, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_16));
        assertEquals(SerializerVersion.V1_15, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_15_2));
        assertEquals(SerializerVersion.V1_14, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_14));
        assertEquals(SerializerVersion.V1_13, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_13_2));
        assertEquals(SerializerVersion.V1_12, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_12_2));
        assertEquals(SerializerVersion.V1_9, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_9));
        assertEquals(SerializerVersion.V1_8, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_8));
        assertEquals(SerializerVersion.V1_7, ViaDialogTagBuilder.serializerFor(ProtocolVersion.v1_7_2));
    }

    @Test
    void componentsMappedFromBaseToTarget() {
        // Legacy colored text survives mapping from an older base version to a newer target
        ViaMultiActionDialog dialog = new ViaMultiActionDialog("test", ViaDialogTagBuilder::deserializeLegacy);
        dialog.title("\u00a7aHello");
        CompoundTag tag = dialog.getDialogTag(ProtocolVersion.v1_20_5, ProtocolVersion.v1_21_6);
        assertEquals("Hello", textOf(tag.get("title")));
        assertEquals("green", ((CompoundTag) tag.get("title")).getString("color"));
    }

    @Test
    void legacyBaseSerializesWithTarget() {
        // A legacy base serializer has no NBT codec; the component is serialized with the target serializer
        Tag tag = ViaDialogTagBuilder.component(ViaDialogTagBuilder.deserializeLegacy("\u00a7aHello"),
                SerializerVersion.V1_8, SerializerVersion.V1_21_6);
        assertEquals("Hello", textOf(tag));
        assertEquals("green", ((CompoundTag) tag).getString("color"));
    }
    @Test
    void itemComponentsSerialized() {
        StructuredDataContainer data = new StructuredDataContainer(new StructuredData<?>[]{
            StructuredData.of(StructuredDataKey.CUSTOM_NAME, new StringTag("{\"text\":\"Named\"}"), -1),
            StructuredData.of(StructuredDataKey.LORE, new Tag[]{new StringTag("Line 1"), new StringTag("Line 2")}, -1)
        });
        com.viaversion.viaversion.api.minecraft.item.StructuredItem item = new com.viaversion.viaversion.api.minecraft.item.StructuredItem(24, 1, data);

        ViaMultiActionDialog dialog = new TestManager().createMultiActionDialog();
        dialog.body(builder -> builder.item().item(item));
        CompoundTag itemTag = dialog.getDialogTag(ProtocolVersion.v1_21_6, ProtocolVersion.v1_21_6)
            .getListTag("body", CompoundTag.class).get(0).getCompoundTag("item");

        CompoundTag components = itemTag.getCompoundTag("components");
        assertNotNull(components);
        assertEquals("{\"text\":\"Named\"}", components.getString("minecraft:custom_name"));
        ListTag<CompoundTag> lore = components.getListTag("minecraft:lore", CompoundTag.class);
        assertNotNull(lore);
        assertEquals(2, lore.size());
        assertEquals("Line 1", textOf(lore.get(0).getListTag("extra", StringTag.class).get(0)));
    }
}
