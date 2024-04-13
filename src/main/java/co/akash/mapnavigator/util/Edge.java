package co.akash.mapnavigator.util;

import lombok.Data;

import java.util.Objects;

@Data
public class Edge {
    private final Vertex to;
    private Integer weight;

    public Edge(Vertex to, Integer weight) {
        this.to = to;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return Objects.equals(to, edge.to);
    }
}