package temp.bpmn2svg.bpmn;

// <bpmn:parallelGateway id="parallelChecksStart"/>

public record ParallelGateway(
        String id
) implements BpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.PARALLEL_GATEWAY;
    }
}
