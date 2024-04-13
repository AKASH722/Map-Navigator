package co.akash.mapnavigator.repo;

import co.akash.mapnavigator.model.BusStation;
import co.akash.mapnavigator.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepo extends JpaRepository<Route, Integer> {
    Optional<Route> findByFromStationAndToStation(BusStation fromStation, BusStation toStation);
}
