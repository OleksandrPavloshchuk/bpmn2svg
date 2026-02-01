package temp.bpmn2svg.translate;

import temp.bpmn2svg.bpmn.Definitions;
import temp.bpmn2svg.bpmn.Process;

import java.util.*;
import java.util.function.Function;

/**
 * This class distributes ids by rows, lanes and columns in lanes for further calculation of SVG positions
 */
public class DistributeNodes {
    private final Definitions definitions;

    private final Map<String, Position> positions = new TreeMap<>();

    private final Map<Integer, Integer> maxColByRow = new HashMap<>();

    private final Map<Integer, Map<String, Integer>> maxColByRowAndLine = new HashMap<>();

    private final Map<String, Integer> laneOffsets = new HashMap<>();

    private final Map<String, Integer> laneWidths = new HashMap<>();

    public DistributeNodes(Definitions definitions) {
        this.definitions = definitions;
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public Map<String, Integer> getLaneOffsets() { return laneOffsets; }
    public Map<String, Integer> getLaneWidths() {
        // Calculate the width of all elements:
        laneOffsets.keySet().forEach(this::getLaneWidth);
        return laneWidths;
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

    public int getLaneWidth(String laneId) {
        return laneWidths.computeIfAbsent(laneId, this::calculateLaneWidth);
    }

    public int getLaneOffset(Process process, String laneId) {
        return laneOffsets.computeIfAbsent(laneId, (k) -> calculateLaneOffset(process, k));
    }

    public int getOffsetInLane(String nodeId) {
        return positions.get(nodeId).col();
    }

    private int calculateLaneWidth(String laneId) {
        return maxColByRowAndLine.values().stream()
                .map(maxColByLineInRow -> maxColByLineInRow.getOrDefault(laneId, 0))
                .max(Integer::compareTo)
                .orElse(0);
    }

    private int calculateLaneOffset(Process process, String laneId) {
        final Set<String> sortedLaneIds = new TreeSet<>(new LanesSupport(process.bpmnObjects()).getLaneIds());
        int result = 0;
        for (final String s : sortedLaneIds) {
            if (s.equals(laneId)) {
                return result;
            }
            result += getLaneWidth(s);
        }
        return result;
    }

    private void perform(Process process) {
        visit(process, process.getStartEvent().id(), 0);
    }

    private void visit(Process process, String id, int row) {
        if (isVisited(id)) {
            return;
        }
        final String laneId = LanesSupport.getLane(process.bpmnObjects().get(id));
        final Map<String, Integer> lineCols = maxColByRowAndLine.computeIfAbsent(row, k -> new HashMap<>());
        final int mapCol = lineCols.computeIfAbsent(laneId, k -> 0);

        positions.put(id, new Position(row, mapCol, laneId));
        final int globalCol = maxColByRow.computeIfAbsent(row, k -> 0);
        maxColByRow.put(row, globalCol + 1);
        lineCols.put(laneId, mapCol + 1);

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

    public record Position(int row, int col, String laneId) {
    }

}
