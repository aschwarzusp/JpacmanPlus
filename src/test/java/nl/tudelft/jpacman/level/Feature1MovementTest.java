package nl.tudelft.jpacman.level;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Gate;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;

/** Integration tests for the Feature 1 map format and movement behaviour. */
class Feature1MovementTest {
    /** A switch toggles on entry, not while the player stays on it. */
    @Test
    void switchTogglesOnlyWhenEntered() {
        PacManSprites sprites = new PacManSprites();
        Level level = createParser(sprites).parseMap(Arrays.asList(
            "#######", "#P1a..#", "#######"));
        Player player = new PlayerFactory(sprites).createPacMan();
        level.registerPlayer(player);
        Gate gate = (Gate) level.getBoard().squareAt(3, 1);
        level.start();
        try {
            level.move(player, Direction.EAST);
            assertTrue(gate.isOpen());

            level.move(player, Direction.NORTH);
            assertTrue(gate.isOpen());

            level.move(player, Direction.EAST);
            assertTrue(gate.isOpen());
            level.move(player, Direction.WEST);
            assertFalse(gate.isOpen());
        } finally {
            level.stop();
        }
    }

    /** A ghost may occupy a switch without activating its circuit. */
    @Test
    void ghostDoesNotActivateSwitch() {
        PacManSprites sprites = new PacManSprites();
        Level level = createParser(sprites).parseMap(Arrays.asList(
            "#######", "#P1a..#", "#######"));
        Ghost ghost = new GhostFactory(sprites).createBlinky();
        ghost.occupy(level.getBoard().squareAt(1, 1));
        Gate gate = (Gate) level.getBoard().squareAt(3, 1);
        level.start();
        try {
            level.move(ghost, Direction.EAST);
            assertFalse(gate.isOpen());
        } finally {
            level.stop();
        }
    }

    /** Collision with a ghost on the switch prevents activation. */
    @Test
    void collisionOnSwitchPreventsActivation() {
        PacManSprites sprites = new PacManSprites();
        Level level = createParser(sprites).parseMap(Arrays.asList(
            "#######", "#P1a..#", "#######"));
        Ghost ghost = new GhostFactory(sprites).createBlinky();
        ghost.occupy(level.getBoard().squareAt(2, 1));
        Player player = new PlayerFactory(sprites).createPacMan();
        level.registerPlayer(player);
        Gate gate = (Gate) level.getBoard().squareAt(3, 1);
        level.start();
        try {
            level.move(player, Direction.EAST);
            assertFalse(player.isAlive());
            assertFalse(gate.isOpen());
        } finally {
            level.stop();
        }
    }

    /** A map with no Feature 1 symbols remains valid. */
    @Test
    void oldMapWithoutCircuitsStillParses() {
        PacManSprites sprites = new PacManSprites();
        Level level = createParser(sprites).parseMap(Arrays.asList(
            "#####", "#P..#", "#####"));
        assertNotNull(level);
    }

    /** A circuit with only a switch is rejected with a configuration error. */
    @Test
    void incompleteCircuitIsRejected() {
        PacManSprites sprites = new PacManSprites();
        MapParser parser = createParser(sprites);
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(Arrays.asList("#####", "#P1.#", "#####")));
    }

    /** Unknown map characters retain the parser's explicit error behaviour. */
    @Test
    void unknownMapSymbolIsRejected() {
        PacManSprites sprites = new PacManSprites();
        MapParser parser = createParser(sprites);
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(Arrays.asList("#####", "#PX.#", "#####")));
    }

    private MapParser createParser(PacManSprites sprites) {
        LevelFactory levels = new LevelFactory(sprites,
            new GhostFactory(sprites), mock(PointCalculator.class));
        return new MapParser(levels, new BoardFactory(sprites));
    }
}
