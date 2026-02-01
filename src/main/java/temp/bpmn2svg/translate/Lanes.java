package temp.bpmn2svg.translate;

import temp.bpmn2svg.bpmn.BpmnObject;
import temp.bpmn2svg.bpmn.UserTask;

import java.util.Collection;
import java.util.Map;

public record Lanes(Map<String, BpmnObject> bpmnObjects) {
    public static final String SYSTEM_LANE_ID = "SYSTEM_LANE_ID";

    public Collection<String> getLines() {
        return bpmnObjects.keySet();
    }

    public String getLane(BpmnObject bpmnObject) {
        return switch (bpmnObject.getType()) {
            case USER_TASK -> ((UserTask) bpmnObject).candidateGroups();
            case END_EVENT, START_EVENT, PARALLEL_GATEWAY, EXCLUSIVE_GATEWAY, SERVICE_TASK -> SYSTEM_LANE_ID;
            default -> throw new IllegalStateException("Unexpected value: " + bpmnObject.getType());
        };
    }

}
