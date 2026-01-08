package temp.bpmn2svg.bpmn;

//<bpmn:startEvent id="startEvent" name="Start"/>

public record StartEvent(
    String id,
    String name
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.START_EVENT;
    }

}
