package temp.bpmn2svg.bpmn;

// <bpmn:userTask id="enterApplication" name="Enter loan application"/>

public record UserTask(
        String id,
        String name
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.USER_TASK;
    }

}
