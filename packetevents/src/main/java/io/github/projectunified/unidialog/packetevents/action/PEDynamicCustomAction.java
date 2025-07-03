package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.projectunified.unidialog.core.action.DynamicCustomAction;

public class PEDynamicCustomAction implements DynamicCustomAction<PEDynamicCustomAction>, PEDialogAction {
    private final String defaultNamespace;
    private ResourceLocation namespacedId;

    public PEDynamicCustomAction(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    @Override
    public PEDynamicCustomAction id(String id) {
        return namespacedId(defaultNamespace, id);
    }

    @Override
    public PEDynamicCustomAction namespacedId(String namespace, String id) {
        this.namespacedId = new ResourceLocation(namespace, id);
        return this;
    }

    @Override
    public Action getAction() {
        if (namespacedId == null) {
            throw new IllegalStateException("Namespaced ID must be set before getting the action");
        }
        return new com.github.retrooper.packetevents.protocol.dialog.action.DynamicCustomAction(namespacedId, null);
    }
}
