package temp.bpmn2svg.bpmn;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BpmnObjectType {
    DEFINITIONS("definitions", BpmnObjectCategory.GROUP),
    PROCESS("process", BpmnObjectCategory.GROUP),
    START_EVENT("startEvent", BpmnObjectCategory.NODE),
    END_EVENT("endEvent", BpmnObjectCategory.NODE),
    SERVICE_TASK("serviceTask", BpmnObjectCategory.NODE),
    USER_TASK("userTask", BpmnObjectCategory.NODE),
    PARALLEL_GATEWAY("parallelGateway", BpmnObjectCategory.NODE),
    EXCLUSIVE_GATEWAY("exclusiveGateway", BpmnObjectCategory.NODE),
    SEQUENCE_FLOW("sequenceFlow", BpmnObjectCategory.LINK)
    ;
    private final BpmnObjectCategory category;
    private final String diagramName;

    BpmnObjectType(String diagramName, BpmnObjectCategory category) {
        this.diagramName = diagramName;
        this.category = category;
    }

    public void verify(String diagramName) {
        if (!this.diagramName.equals(diagramName)) {
            throw new IllegalArgumentException("Unexpected diagram name: " + diagramName);
        }
    }

    public static Optional<BpmnObjectType> findByDiagramName(String diagramName) {
        return  Stream.of(BpmnObjectType.values())
                .filter( bpmnObjectType -> bpmnObjectType.diagramName.equals(diagramName))
                .findFirst();

    }

    public boolean isNode() {
        return category==BpmnObjectCategory.NODE;
    }

}
