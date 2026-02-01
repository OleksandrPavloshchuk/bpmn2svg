package temp.bpmn2svg.translate;

import temp.bpmn2svg.svg.SvgElementsSizes;
import temp.bpmn2svg.svg.SvgPoint;
import temp.bpmn2svg.bpmn.Process;

import java.util.HashMap;
import java.util.Map;

public record TranslateDistribution2SvgCoordinates(Process process, DistributeNodes distributeNodes) {

    public Map<String, SvgPoint> apply() {
        final Map<String, DistributeNodes.Position> positions = distributeNodes.getPositions();
        final Map<String, SvgPoint> result = new HashMap<>();
        positions.forEach((id, position) -> result.put(id, getSvgPoint(id, position)));
        return result;
    }

    private SvgPoint getSvgPoint(String nodeId, DistributeNodes.Position position) {
        final String laneId = Lanes.getLane(process.bpmnObjects().get(nodeId));
        final int laneOffset = distributeNodes.getLaneOffset(process, laneId);
        final int offsetInLane = distributeNodes.getOffsetInLane(nodeId);
        final int x = (laneOffset + offsetInLane) * SvgElementsSizes.X_STEP;
        final int y = (1 + position.row()) * SvgElementsSizes.Y_STEP;
        return new SvgPoint(x, y);
    }

}
