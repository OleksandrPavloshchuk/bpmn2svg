package temp.bpmn2svg.translate;

import temp.bpmn2svg.bpmn.Definitions;
import temp.bpmn2svg.bpmn.Process;

import java.util.*;
import java.util.function.Function;

/**
 * This class distributes ids by rows and columns for further calculation of SVG positions
 */
public class DistributeNodes {
    private final Definitions definitions;

    private final Map<String, Position> positions = new TreeMap<>();

    public DistributeNodes(Definitions definitions) {
        this.definitions = definitions;
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public DistributeNodes perform() {
        // TODO Only one process
        this.definitions.processes().forEach(this::perform);
        return this;
    }

    public int getMaxRow() {
        return getMax(Position::row);
    }

    public int getMaxCol() {
        return getMax(Position::col);
    }

    private void perform(Process process) {
        visit(process, process.getStartEvent().id(), 0, 0);
    }

    private void visit(Process process, String id, int row, int col) {
        if (isVisited(id)) {
            return;
        }
        positions.put(id, new Position(row, col));

        final List<String> linkedIds = new ArrayList<>(process.getLinkedNodeIds(id));
        for( int i = 0; i<linkedIds.size(); i++ ) {
            visit(process, linkedIds.get(i), row+1, i);
        }

    }

    private boolean isVisited(String id) {
        return positions.containsKey(id);
    }

    private int getMax(Function<Position, Integer> getter) {
        return positions.values().stream()
                .map(getter)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
    }

    public record Position(int row, int col) {
    }

}
