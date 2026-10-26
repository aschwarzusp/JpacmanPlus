package nl.tudelft.jpacman.board;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Graphics;
import org.junit.jupiter.api.Test;
import nl.tudelft.jpacman.sprite.Sprite;

/** Unit tests for the independent circuit and pending-gate behaviour. */
class CircuitTest {
    private static final Sprite TEST_SPRITE = new TestSprite();

    /** Circuits start closed and toggling one does not affect the other. */
    @Test
    void circuitsAreIndependentAndStartClosed() {
        Circuit first = new Circuit(1);
        Circuit second = new Circuit(2);
        Gate firstGate = new Gate(first, TEST_SPRITE, TEST_SPRITE);
        Gate secondGate = new Gate(second, TEST_SPRITE, TEST_SPRITE);
        Unit unit = new TestUnit();

        assertFalse(first.isOpen());
        assertFalse(firstGate.isAccessibleTo(unit));
        assertFalse(secondGate.isAccessibleTo(unit));

        first.toggle();

        assertTrue(first.isOpen());
        assertTrue(firstGate.isAccessibleTo(unit));
        assertFalse(second.isOpen());
        assertFalse(secondGate.isAccessibleTo(unit));
    }

    /** Gates in one circuit receive the same command but may be pending differently. */
    @Test
    void gatesShareCommandsButKeepTheirOwnOccupancyState() {
        Circuit circuit = new Circuit(1);
        Gate emptyGate = new Gate(circuit, TEST_SPRITE, TEST_SPRITE);
        Gate occupiedGate = new Gate(circuit, TEST_SPRITE, TEST_SPRITE);
        Unit occupant = new TestUnit();
        circuit.toggle();
        occupant.occupy(occupiedGate);

        circuit.toggle();

        assertFalse(emptyGate.isOpen());
        assertFalse(emptyGate.isPending());
        assertFalse(occupiedGate.isOpen());
        assertTrue(occupiedGate.isPending());
    }

    /** A pending gate closes only after the last of its occupants has left. */
    @Test
    void pendingCloseWaitsForAllOccupants() {
        Circuit circuit = new Circuit(1);
        Gate gate = new Gate(circuit, TEST_SPRITE, TEST_SPRITE);
        Unit first = new TestUnit();
        Unit second = new TestUnit();
        Unit entrant = new TestUnit();
        circuit.toggle();
        first.occupy(gate);
        second.occupy(gate);

        circuit.toggle();

        assertTrue(gate.isPending());
        assertFalse(gate.isAccessibleTo(entrant));
        first.occupy(new GroundSquare());
        assertTrue(gate.isPending());
        second.leaveSquare();
        assertFalse(gate.isPending());
        assertFalse(gate.isOpen());
        assertFalse(gate.isAccessibleTo(entrant));
    }

    /** Opening a circuit cancels its gates' pending close state. */
    @Test
    void openingCancelsPendingClose() {
        Circuit circuit = new Circuit(1);
        Gate gate = new Gate(circuit, TEST_SPRITE, TEST_SPRITE);
        Unit occupant = new TestUnit();
        circuit.toggle();
        occupant.occupy(gate);
        circuit.toggle();
        assertTrue(gate.isPending());

        circuit.toggle();

        assertFalse(gate.isPending());
        assertTrue(gate.isOpen());
        assertTrue(gate.getOccupants().contains(occupant));
    }

    private static final class TestUnit extends Unit {
        @Override
        public Sprite getSprite() {
            return TEST_SPRITE;
        }
    }

    private static final class GroundSquare extends Square {
        @Override
        public boolean isAccessibleTo(Unit unit) {
            return true;
        }

        @Override
        public Sprite getSprite() {
            return TEST_SPRITE;
        }
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
