package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationRequest target;

    public FavoriteResponse(Long id, StationResponse source, StationRequest target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationRequest getTarget() {
        return target;
    }
}
