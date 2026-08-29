package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;

/**
 * A PacketEvents-based body for a dialog.
 */
public interface PEDialogBody {
    /**
     * Get the PacketEvents dialog body
     *
     * @return the dialog body
     */
    DialogBody getDialogBody();
}
