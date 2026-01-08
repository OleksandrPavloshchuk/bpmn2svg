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

    private final Map<Integer, Integer> maxColByRow = new HashMap<>();

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
        visit(process, process.getStartEvent().id(), 0);
    }

    private void visit(Process process, String id, int row) {
        if (isVisited(id)) {
            return;
        }
        int col = maxColByRow.computeIfAbsent(row, k -> 0);
        positions.put(id, new Position(row, col));
        maxColByRow.put(row, col + 1);

        process.getLinkedNodeIds(id).forEach(
                linkedId -> visit(process, linkedId, row + 1)
        );
    }

    private boolean isVisited(String id) {
        return positions.containsKey(id);
    }

    private int getMax(Function<Position, Integer> getter) {
        return positions.values().stream()
                .map(getter)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public record Position(int row, int col) {
    }

}
