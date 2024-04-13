package co.akash.mapnavigator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RouteRequest {
    private String source;
    private String destination;
    private Integer distance;
}