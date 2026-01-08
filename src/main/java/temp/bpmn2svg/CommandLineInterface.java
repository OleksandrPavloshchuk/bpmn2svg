package temp.bpmn2svg;

import temp.bpmn2svg.translate.TranslateBpmn2Svg;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Assumes valid BPMN XML.
 * Structural validation is delegated to BPMN engine (e.g. Camunda).
 */
public class CommandLineInterface {
    private static final Logger LOGGER = Logger.getLogger(CommandLineInterface.class.getName());

    public static void main(String[] args) {
        try {
            new TranslateBpmn2Svg(System.in,System.out).apply();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Conversion error", ex);
        }
    }
}
