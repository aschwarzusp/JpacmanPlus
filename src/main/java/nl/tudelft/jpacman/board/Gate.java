package nl.tudelft.jpacman.board;

import java.awt.Color;
import nl.tudelft.jpacman.sprite.LabelSprite;
import nl.tudelft.jpacman.sprite.Sprite;

/** A gate whose entry permission is controlled by a circuit. */
public class Gate extends Square {
    private final Sprite closedSprite;
    private final Sprite openSprite;
    private final Sprite pendingSprite;
    private boolean open;
    private boolean pending;

    /**
     * Creates a gate in the closed state and registers it with its circuit.
     *
     * @param circuit circuit controlling this gate
     * @param openBackground background used for an open gate
     * @param closedBackground background used for a closed gate
     */
    public Gate(Circuit circuit, Sprite openBackground, Sprite closedBackground) {
        this.openSprite = new LabelSprite(openBackground,
            String.valueOf(circuit.getId()), Color.BLACK);
        this.closedSprite = new LabelSprite(closedBackground,
            String.valueOf(circuit.getId()), Color.WHITE);
        this.pendingSprite = new LabelSprite(closedBackground,
            circuit.getId() + "*", Color.YELLOW);
        this.open = false;
        this.pending = false;
        circuit.addGate(this);
    }

    /** Opens the gate and cancels any pending closure. */
    void open() {
        open = true;
        pending = false;
    }

    /**
     * Closes an empty gate immediately or schedules closure after occupants leave.
     */
    void close() {
        if (getOccupants().isEmpty()) {
            open = false;
            pending = false;
        } else {
            open = false;
            pending = true;
        }
    }

    /** Completes a pending closure when the last occupant leaves. */
    void unitLeft() {
        if (pending && getOccupants().isEmpty()) {
            open = false;
            pending = false;
        }
    }

    /** @return true when the gate is fully open and accepts new entrants */
    public boolean isOpen() {
        return open && !pending;
    }

    /** @return true while the gate waits for its current occupants to leave */
    public boolean isPending() {
        return pending;
    }

    @Override
    public boolean isAccessibleTo(Unit unit) {
        return isOpen();
    }

    @Override
    public Sprite getSprite() {
        if (pending) {
            return pendingSprite;
        }
        return open ? openSprite : closedSprite;
    }
}
