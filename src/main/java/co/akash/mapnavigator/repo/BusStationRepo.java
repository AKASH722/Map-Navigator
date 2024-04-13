package co.akash.mapnavigator.repo;

import co.akash.mapnavigator.model.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStationRepo extends JpaRepository<BusStation, Integer> {
    Optional<BusStation> findByNameIgnoreCase(String name);
}
