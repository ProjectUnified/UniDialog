package io.github.projectunified.unidialog.viaversion;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viaversion.api.minecraft.data.StructuredData;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataContainer;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataKey;
import com.viaversion.viaversion.api.minecraft.item.StructuredItem;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.viaversion.dialog.ViaMultiActionDialog;
import io.github.projectunified.unidialog.viaversion.dialog.ViaNoticeDialog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the exact NBT structure of dialog bodies and actions against the vanilla
 * {@code minecraft:dialog} format read by {@code RegistryDataRewriter#updateDialog} and
 * ViaBackwards' {@code Dialog}/{@code Button}/{@code TextWidget}/{@code ItemWidget} parsers.
 */
class ViaDialogStructureTest {

    private static final ProtocolVersion TARGET = ProtocolVersion.v1_21_6;

    private static String textOf(Tag tag) {
        if (tag instanceof StringTag stringTag) {
            return stringTag.getValue();
        }
        return ((CompoundTag) tag).getString("text");
    }

    private static ViaMultiActionDialog dialog() {
        return new ViaMultiActionDialog("test", ViaDialogTagBuilder::deserializeLegacy, SerializerVersion.V1_21_6);
    }

    private static CompoundTag buttonTag(ViaMultiActionDialog dialog, int index) {
        return dialog.getDialogTag(TARGET).getListTag("actions", CompoundTag.class).get(index);
    }

    private static CompoundTag actionTag(CompoundTag button) {
        return button.getCompoundTag("action");
    }

    @Test
    void plainMessageBodyStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.text().text("Hello world").width(150));
        CompoundTag body = dialog.getDialogTag(TARGET).getListTag("body", CompoundTag.class).get(0);

        assertEquals(3, body.size(), "plain_message body must only contain type, contents and width");
        assertEquals("minecraft:plain_message", body.getString("type"));
        assertEquals("Hello world", textOf(body.get("contents")));
        assertEquals(150, body.getInt("width"));
    }

    @Test
    void plainMessageBodyDefaults() {
        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.text().text("Default"));
        CompoundTag body = dialog.getDialogTag(TARGET).getListTag("body", CompoundTag.class).get(0);
        assertEquals(200, body.getInt("width"), "default plain_message width is 200");
    }

    @Test
    void itemBodyStructure() {
        StructuredDataContainer data = new StructuredDataContainer(new StructuredData<?>[]{
            StructuredData.of(StructuredDataKey.CUSTOM_NAME, new StringTag("{\"text\":\"Named\"}"), -1),
            StructuredData.of(StructuredDataKey.LORE, new Tag[]{new StringTag("Line 1")}, -1)
        });
        StructuredItem item = new StructuredItem(24, 3, data);

        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.item().item(item)
            .description(text -> text.text("An item").width(90))
            .showDecorations(false)
            .showTooltip(false)
            .width(32)
            .height(32));
        CompoundTag body = dialog.getDialogTag(TARGET).getListTag("body", CompoundTag.class).get(0);

        assertEquals(7, body.size(), "item body must contain type, item, description, show_decorations, show_tooltip, width and height");
        assertEquals("minecraft:item", body.getString("type"));

        CompoundTag itemTag = body.getCompoundTag("item");
        assertNotNull(itemTag.getString("id"), "item id must be present");
        assertEquals(3, itemTag.getInt("count"));
        CompoundTag components = itemTag.getCompoundTag("components");
        assertNotNull(components);
        assertEquals("{\"text\":\"Named\"}", components.getString("minecraft:custom_name"));
        CompoundTag loreEntry = components.getListTag("minecraft:lore", CompoundTag.class).get(0);
        assertEquals("Line 1", textOf(loreEntry.getListTag("extra", StringTag.class).get(0)));

        CompoundTag description = body.getCompoundTag("description");
        assertNotNull(description);
        assertEquals("An item", textOf(description.get("contents")));
        assertEquals(90, description.getInt("width"));

        assertFalse(body.getBoolean("show_decorations"));
        assertFalse(body.getBoolean("show_tooltip"));
        assertEquals(32, body.getInt("width"));
        assertEquals(32, body.getInt("height"));
    }

    @Test
    void itemBodyUsesViaItemTarget() {
        // The item body serializes the ViaItem with the dialog's target serializer
        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.item().item(target -> {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putString("id", "minecraft:test");
            itemTag.putString("target", target.name());
            return itemTag;
        }));
        CompoundTag itemTag = dialog.getDialogTag(TARGET).getListTag("body", CompoundTag.class).get(0).getCompoundTag("item");
        assertEquals("minecraft:test", itemTag.getString("id"));
        assertEquals(SerializerVersion.V1_21_6.name(), itemTag.getString("target"));
    }

    @Test
    void itemBodyDefaults() {
        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.item().item(new StructuredItem(24, 1)));
        CompoundTag body = dialog.getDialogTag(TARGET).getListTag("body", CompoundTag.class).get(0);

        assertTrue(body.getBoolean("show_decorations"), "show_decorations defaults to true");
        assertTrue(body.getBoolean("show_tooltip"), "show_tooltip defaults to true");
        assertEquals(16, body.getInt("width"), "default item width is 16");
        assertEquals(16, body.getInt("height"), "default item height is 16");
        assertFalse(body.contains("description"), "description omitted when not set");
    }

    @Test
    void buttonStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Label").tooltip("Tip").width(120));
        CompoundTag button = buttonTag(dialog, 0);

        assertEquals(3, button.size(), "button must contain label, tooltip and width when no action");
        assertEquals("Label", textOf(button.get("label")));
        assertEquals("Tip", textOf(button.get("tooltip")));
        assertEquals(120, button.getInt("width"));
        assertNull(button.get("action"), "no action key when the button has no action");
    }

    @Test
    void buttonDefaults() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.runCommand("x"));
        CompoundTag button = buttonTag(dialog, 0);
        assertEquals("Action", textOf(button.get("label")), "default label is Action");
        assertEquals(150, button.getInt("width"), "default button width is 150");
        assertFalse(button.contains("tooltip"), "tooltip omitted when not set");
    }

    @Test
    void openUrlActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Open").openUrl("https://example.com"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size(), "open_url action must only contain type and url");
        assertEquals("minecraft:open_url", action.getString("type"));
        assertEquals("https://example.com", action.getString("url"));
    }

    @Test
    void runCommandActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Run").runCommand("/say hi"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:run_command", action.getString("type"));
        assertEquals("/say hi", action.getString("command"));
    }

    @Test
    void suggestCommandActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Suggest").suggestCommand("help"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:suggest_command", action.getString("type"));
        assertEquals("help", action.getString("command"));
    }

    @Test
    void copyToClipboardActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Copy").copyToClipboard("copied"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:copy_to_clipboard", action.getString("type"));
        assertEquals("copied", action.getString("value"));
    }

    @Test
    void dynamicRunCommandActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Dynamic").dynamicRunCommand("tp @p {x}"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:dynamic/run_command", action.getString("type"));
        assertEquals("tp @p {x}", action.getString("template"));
    }

    @Test
    void dynamicCustomActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Custom").dynamicCustom("myplugin", "action_id"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(3, action.size());
        assertEquals("minecraft:dynamic/custom", action.getString("type"));
        assertEquals("myplugin:action_id", action.getString("id"));
        assertNotNull(action.getCompoundTag("additions"), "additions must be present");
        assertEquals(0, action.getCompoundTag("additions").size(), "additions start empty");
    }

    @Test
    void showDialogInlineActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        ViaNoticeDialog child = new ViaNoticeDialog("test", ViaDialogTagBuilder::deserializeLegacy, SerializerVersion.V1_21_6);
        child.title("Target");
        dialog.action(action -> action.label("Open").showDialog(child));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:show_dialog", action.getString("type"));
        CompoundTag dialogTag = action.getCompoundTag("dialog");
        assertNotNull(dialogTag, "dialog must be an inlined compound tag");
        assertEquals("minecraft:notice", dialogTag.getString("type"));
    }

    @Test
    void showDialogRegistryActionStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.action(action -> action.label("Open").showDialog("minecraft", "dialog_id"));
        CompoundTag action = actionTag(buttonTag(dialog, 0));

        assertEquals(2, action.size());
        assertEquals("minecraft:show_dialog", action.getString("type"));
        assertEquals("minecraft:dialog_id", action.getString("dialog"));
    }

    @Test
    void itemBodyRequiresItem() {
        // An item body without an item fails at tag construction; ViaDialogOpener turns it into a
        // failed open instead of throwing
        ViaMultiActionDialog dialog = dialog();
        dialog.body(builder -> builder.item());
        assertThrows(IllegalStateException.class, () -> dialog.getDialogTag(TARGET));
    }

    @Test
    void multiActionDialogStructure() {
        ViaMultiActionDialog dialog = dialog();
        dialog.columns(3)
            .action(action -> action.label("One").runCommand("one"))
            .action(action -> action.label("Two").runCommand("two"))
            .exitAction(action -> action.label("Exit").runCommand("exit"));
        CompoundTag tag = dialog.getDialogTag(TARGET);

        assertEquals(8, tag.size(), "multi_action dialog must contain type, title, can_close_with_escape, pause, after_action, actions, exit_action and columns");
        assertEquals("minecraft:multi_action", tag.getString("type"));
        ListTag<CompoundTag> actions = tag.getListTag("actions", CompoundTag.class);
        assertEquals(2, actions.size());
        assertEquals("one", actions.get(0).getCompoundTag("action").getString("command"));
        assertEquals("two", actions.get(1).getCompoundTag("action").getString("command"));
        assertEquals("exit", tag.getCompoundTag("exit_action").getCompoundTag("action").getString("command"));
        assertEquals(3, tag.getInt("columns"));
    }
}
