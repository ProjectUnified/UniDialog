package io.github.projectunified.unidialog.viaversion.spigot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link ViaItemUtil} paths that do not require a running server.
 */
class ViaItemUtilTest {

    @Test
    void nullInputReturnsNull() {
        assertNull(ViaItemUtil.fromItemStack(null));
    }
}
