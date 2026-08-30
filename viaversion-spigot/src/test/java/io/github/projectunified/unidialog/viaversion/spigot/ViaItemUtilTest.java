package io.github.projectunified.unidialog.viaversion.spigot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link ViaItemUtil} paths that do not require a running server.
 */
class ViaItemUtilTest {

    @Test
    void nullInputReturnsNull() {
        assertNotNull(ViaItemUtil.fromItemStack(null));
    }
}
