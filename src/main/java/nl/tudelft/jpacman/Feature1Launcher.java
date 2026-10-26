package nl.tudelft.jpacman;



/** Launcher for the Feature 1 demonstration map. */
public class Feature1Launcher extends Launcher {
    /**
     * Starts the Feature 1 demonstration.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Feature1Launcher().withMapFile("/feature1.txt").launch();
    }
}
