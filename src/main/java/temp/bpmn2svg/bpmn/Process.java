package temp.bpmn2svg.bpmn;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record Process (
        String id,
        String name,
        Map<String, BpmnObject> bpmnObjects
) implements NamedBpmnObject {
    public static final String KEY_SYSTEM = "system";

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.PROCESS;
    }

    public BpmnObject getStartEvent() {
        return bpmnObjects.values().stream()
                .filter(bpmnObject -> bpmnObject.getType() == BpmnObjectType.START_EVENT)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no start event"));
    }

    public Set<String> getLinkedNodeIds(String startNodeId) {
        return bpmnObjects.values().stream()
                .filter(bpmnObject -> bpmnObject.getType() == BpmnObjectType.SEQUENCE_FLOW)
                .map(SequenceFlow.class::cast)
                .filter(link -> link.sourceRef().equals(startNodeId))
                .map(SequenceFlow::targetRef)
                .collect(Collectors.toSet());
    }

}
