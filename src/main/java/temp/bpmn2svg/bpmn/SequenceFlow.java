package temp.bpmn2svg.bpmn;

/*
        <bpmn:sequenceFlow id="scoringDecision-manualReview" sourceRef="scoringDecision" targetRef="manualReview">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">
                ${scoringResult == 'MANUAL'}
            </bpmn:conditionExpression>
        </bpmn:sequenceFlow>
 */

public record SequenceFlow(
        String id,
        String sourceRef,
        String targetRef,
        String conditionalExpression
) implements BpmnLink {

    public SequenceFlow(
            String id,
            String sourceRef,
            String targetRef
    ) {
        this(id, sourceRef, targetRef, null);
    }

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.SEQUENCE_FLOW;
    }

}
