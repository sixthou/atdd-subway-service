package nextstep.subway.fare.domain;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;
    private final LineFarePolicy lineFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy, LineFarePolicy lineFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.lineFarePolicy = lineFarePolicy;
    }

    public Fare calculate(Distance distance, List<Line> lines) {
        Fare distanceFare = distanceFarePolicy.calculate(distance);
        Fare lineFare = lineFarePolicy.calculate(lines);
        return distanceFare.add(lineFare);
    }
}
