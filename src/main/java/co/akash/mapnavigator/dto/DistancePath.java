package co.akash.mapnavigator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class DistancePath {
    private List<String> path;
    private Integer distance;
}
