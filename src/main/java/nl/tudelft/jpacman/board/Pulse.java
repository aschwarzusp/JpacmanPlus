package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

/** A permanent floor switch that toggles one circuit when entered by a live player. */
public class Pulse extends Square {
    private final Circuit circuit;
    private final Sprite sprite;

    /**
     * Creates a switch and registers it with its circuit.
     *
     * @param controller the circuit toggled by this switch
     * @param switchSprite visual representation of the switch
     */
    public Pulse(Circuit controller, Sprite switchSprite) {
        this.circuit = controller;
        this.sprite = switchSprite;
        controller.addPulse(this);
    }

    /**
     * Toggles this switch's circuit for a living player.
     *
     * @param player the player that entered the switch
     */
    public void activate(Player player) {
        if (player.isAlive()) {
            if (circuit.msegundos == 0 ) {
	    	circuit.toggle();
	    }
	    circuit.msegundos += 5000;
        }
    }

    @Override
    public boolean isAccessibleTo(Unit unit) {
        return true;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
