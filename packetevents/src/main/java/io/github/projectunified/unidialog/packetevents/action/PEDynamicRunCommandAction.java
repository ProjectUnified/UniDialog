package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.DialogTemplate;
import io.github.projectunified.unidialog.core.action.DynamicRunCommandAction;

public class PEDynamicRunCommandAction implements DynamicRunCommandAction<PEDynamicRunCommandAction>, PEDialogAction {
    private String template;

    @Override
    public PEDynamicRunCommandAction template(String template) {
        this.template = template;
        return this;
    }

    @Override
    public Action getAction() {
        if (template == null) {
            throw new IllegalStateException("Template must be set before getting the action");
        }
        return new com.github.retrooper.packetevents.protocol.dialog.action.DynamicRunCommandAction(new DialogTemplate(template));
    }
}
