package nl.tudelft.jpacman.board;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controls the gates belonging to one independent switch circuit.
 */
public class Circuit {
    private final int id;
    private final List<Gate> gates = new ArrayList<>();
    private final List<Switch> switches = new ArrayList<>();
    private final List<Pulse> pulses = new ArrayList<>();
    private boolean open;
    public int segundos = 0;

    /**
     * Creates a new circuit whose gates start closed.
     *
     * @param circuitId identifier used by map symbols and visual labels
     */
    public Circuit(int circuitId) {
        this.id = circuitId;
        this.open = false;
    }

    /** @return the circuit identifier */
    public int getId() {
        return id;
    }

    /** @return whether the latest circuit command is open */
    public boolean isOpen() {
        return open;
    }

    /** @return whether this circuit occurs in the current map */
    public boolean isUsed() {
        return !gates.isEmpty() || !switches.isEmpty();
    }

    /** @return whether this circuit has at least one gate */
    public boolean hasGates() {
        return !gates.isEmpty();
    }

    /** @return whether this circuit has at least one switch */
    public boolean hasSwitches() {
        return !switches.isEmpty();
    }

    void addGate(Gate gate) {
        gates.add(gate);
    }

    void addSwitch(Switch circuitSwitch) {
        switches.add(circuitSwitch);
    }

    /** Toggles the command and updates every gate in this circuit. */
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

    public void pulse() {
    	if (segundos == 0) this.toggle();
	this.segundos += 5;
	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	service.schedule(new PulseRenew(service, this), 5, TimeUnit.SECONDS);
    }

    private final class PulseRenew
    {
    	private final ScheduledExecutorService service;
	PulseRenew(ScheduledExecutorService service, Circuit circuit)
	{
		this.service = service;
		circuit.segundos -= 5;
		if (circuit.segundos == 0) circuit.toggle();
	}
    }
}
