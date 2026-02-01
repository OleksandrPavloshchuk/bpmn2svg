package temp.bpmn2svg.bpmn;

public record UserTask(
        String id,
        String name,
        String candidateGroups
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.USER_TASK;
    }

}
