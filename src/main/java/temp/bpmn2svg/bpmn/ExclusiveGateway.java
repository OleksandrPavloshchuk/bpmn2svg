package temp.bpmn2svg.bpmn;

// <bpmn:exclusiveGateway id="scoringDecision" name="Scoring decision"/>

public record ExclusiveGateway(
       String id,
       String name
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.EXCLUSIVE_GATEWAY;
    }
}
