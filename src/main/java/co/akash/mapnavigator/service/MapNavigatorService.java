package co.akash.mapnavigator.service;

import co.akash.mapnavigator.dto.DistancePath;
import co.akash.mapnavigator.dto.RouteRequest;
import co.akash.mapnavigator.dto.common.CommonErrorResponse;
import co.akash.mapnavigator.dto.common.CommonResponse;
import co.akash.mapnavigator.model.BusStation;
import co.akash.mapnavigator.model.Route;
import co.akash.mapnavigator.repo.BusStationRepo;
import co.akash.mapnavigator.repo.RouteRepo;
import co.akash.mapnavigator.util.Graph;
import co.akash.mapnavigator.util.GraphUtils;
import co.akash.mapnavigator.util.Vertex;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapNavigatorService {
    private final BusStationRepo busStationRepo;
    private final RouteRepo routeRepo;
    private final GraphUtils graphUtils;
    private Graph graph;

    @PostConstruct
    private void postConstruct() {
        this.graph = graphUtils.buildGraph(busStationRepo.findAll(), routeRepo.findAll());
    }


    public CommonResponse<?> getShortestRoute(String source, String destination) {
        GraphUtils.Pair<ArrayList<Vertex>, Integer> shortestPathDistance = graphUtils.shortestPathBetween(graph, graph.getVertex(source.toLowerCase()), graph.getVertex(destination.toLowerCase()));

        if (shortestPathDistance.getValue() == Integer.MAX_VALUE) {
            return new CommonResponse<>(
                DistancePath.builder()
                    .distance(Integer.MAX_VALUE)
                    .path(
                        Collections.singletonList("No such route exist")
                    )
                    .build()
            );
        }

        return new CommonResponse<>(
            DistancePath.builder()
                .distance(shortestPathDistance.getValue())
                .path(
                    shortestPathDistance.getKey().stream()
                        .map(Vertex::getValue)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                .build()
        );
    }

    public String[] getAllStations() {
        List<String> stationNames = busStationRepo.findAll().stream()
            .map(BusStation::getName)
            .toList();

        return stationNames.toArray(new String[0]);
    }


    public CommonResponse<?> addRoute(RouteRequest routeRequest) {
        if (routeRequest.getDistance() <= 0) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "Distance should be positive.",
                    "Distance should be positive.",
                    "Distance should be positive."
                )
            );
        }
        String sourceName = routeRequest.getSource();
        BusStation source = busStationRepo.findByNameIgnoreCase(sourceName)
            .orElseGet(() -> busStationRepo.save(
                BusStation.builder()
                    .name(sourceName)
                    .build()
            ));

        String destinationName = routeRequest.getDestination();
        BusStation destination = busStationRepo.findByNameIgnoreCase(destinationName)
            .orElseGet(() -> busStationRepo.save(
                BusStation.builder()
                    .name(destinationName)
                    .build()
            ));

        if (routeRepo.findByFromStationAndToStation(source, destination).isPresent()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "Route already exists.",
                    "Route already exists.",
                    "Route already exists."
                )
            );
        }
        routeRepo.save(
            Route.builder()
                .fromStation(source)
                .toStation(destination)
                .distance(routeRequest.getDistance())
                .build()
        );
        this.graph = graphUtils.buildGraph(busStationRepo.findAll(), routeRepo.findAll());
        return new CommonResponse<>(
            "Successfully added"
        );
    }
}
