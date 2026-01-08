package temp.bpmn2svg.bpmn;

//<bpmn:endEvent id="endEvent" name="End"/>

public record EndEvent(
    String id,
    String name
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.END_EVENT;
    }
}
