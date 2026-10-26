package nl.tudelft.jpacman.board;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Graphics;
import org.junit.jupiter.api.Test;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

/** Unit tests for switch activation rules. */
class SwitchTest {
    private static final Sprite TEST_SPRITE = new TestSprite();

    /** A live player toggles the switch's circuit on each activation. */
    @Test
    void livePlayerTogglesCircuit() {
        Circuit circuit = new Circuit(1);
        Switch firstSwitch = new Switch(circuit, TEST_SPRITE);
        Switch secondSwitch = new Switch(circuit, TEST_SPRITE);
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(true);

        firstSwitch.activate(player);
        assertTrue(circuit.isOpen());
        secondSwitch.activate(player);
        assertFalse(circuit.isOpen());
    }

    /** A dead player cannot activate a switch after a collision. */
    @Test
    void deadPlayerDoesNotToggleCircuit() {
        Circuit circuit = new Circuit(1);
        Switch floorSwitch = new Switch(circuit, TEST_SPRITE);
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(false);

        floorSwitch.activate(player);

        assertFalse(circuit.isOpen());
    }

    private static final class TestSprite implements Sprite {
        @Override
        public void draw(Graphics graphics, int x, int y, int width, int height) {
            // No drawing is needed in these state tests.
        }

        @Override
        public Sprite split(int x, int y, int width, int height) {
            return this;
        }

        @Override
        public int getWidth() {
            return 16;
        }

        @Override
        public int getHeight() {
            return 16;
        }
    }
}
