package temp.bpmn2svg.translate;

import temp.bpmn2svg.svg.SvgElementsSizes;
import temp.bpmn2svg.svg.SvgPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TranslateDistribution2SvgCoordinates
        implements Function<Map<String, DistributeNodes.Position>, Map<String, SvgPoint>> {

    @Override
    public Map<String, SvgPoint> apply(Map<String, DistributeNodes.Position> src) {
        final Map<String, SvgPoint> result = new HashMap<>();
        src.forEach((id, position) -> result.put(id, getSvgPoint(src, position)));
        return result;
    }

    private static SvgPoint getSvgPoint(Map<String, DistributeNodes.Position> src, DistributeNodes.Position position) {
        final int colsCount = getColsCount(position.row(), src);
        final double offset = ((double) colsCount) / 2;
        final double x = (position.col() + 2 - offset) * SvgElementsSizes.X_STEP;
        return new SvgPoint(
                (int) x,
                (1 + position.row()) * SvgElementsSizes.Y_STEP);
    }

    private static int getColsCount(int row, Map<String, DistributeNodes.Position> src) {
        return src.values().stream()
                .filter(position -> position.row() == row)
                .map(DistributeNodes.Position::col)
                .max(Integer::compare)
                .orElse(-1) + 1;
    }
}
