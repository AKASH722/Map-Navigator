package co.akash.mapnavigator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "from_station_id", referencedColumnName = "id")
    private BusStation fromStation;

    @ManyToOne
    @JoinColumn(name = "to_station_id", referencedColumnName = "id")
    private BusStation toStation;

    private Integer distance;
}
