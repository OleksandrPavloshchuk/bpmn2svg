package temp.bpmn2svg.bpmn;


// <bpmn:serviceTask camunda:class="tutorial.camunda.consumer.loans.tasks.AggregateScoringDelegate" id="scoring" name="Aggregate scoring"/>

public record ServiceTask(
        String id,
        String name,
        String javaClass
) implements NamedBpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.SERVICE_TASK;
    }

}
