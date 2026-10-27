package nl.tudelft.jpacman.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the gates belonging to one independent switch pulse.
 */
public class Pulse {
    private final int id;
    private final List<Gate> gates = new ArrayList<>();
    private final List<Switch> switches = new ArrayList<>();
    private boolean open;

    /**
     * Creates a new pulse whose gates start closed.
     *
     * @param pulseId identifier used by map symbols and visual labels
     */
    public Pulse(int pulseId) {
        this.id = pulseId;
        this.open = false;
    }

    /** @return the pulse identifier */
    public int getId() {
        return id;
    }

    /** @return whether the latest pulse command is open */
    public boolean isOpen() {
        return open;
    }

    /** @return whether this pulse occurs in the current map */
    public boolean isUsed() {
        return !gates.isEmpty() || !switches.isEmpty();
    }

    /** @return whether this pulse has at least one gate */
    public boolean hasGates() {
        return !gates.isEmpty();
    }

    /** @return whether this pulse has at least one switch */
    public boolean hasSwitches() {
        return !switches.isEmpty();
    }

    void addGate(Gate gate) {
        gates.add(gate);
    }

    void addSwitch(Switch pulseSwitch) {
        switches.add(pulseSwitch);
    }

    /** Toggles the command and updates every gate in this pulse. */
    public void toggle() {
        open = !open;
        for (Gate gate : gates) {
            if (open) {
                gate.open();
            } else {
                gate.close();
            }
        }
    }
}
