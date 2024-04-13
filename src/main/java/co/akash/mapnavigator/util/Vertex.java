package co.akash.mapnavigator.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Vertex {
    private final ArrayList<Edge> edges;
    private final String value;

    public Vertex(String value) {
        this.value = value;
        this.edges = new ArrayList<>();
    }

    public boolean addEdge(Vertex to, Integer weight) {
        Edge newEdge = new Edge(to, weight);
        for (Edge edge : edges) {
            if (edge.equals(newEdge)) {
                edge.setWeight(weight);
                return true;
            }
        }
        edges.add(newEdge);
        return edges.contains(newEdge);
    }

    public boolean removeEdge(Vertex to) {
        return this.edges.removeIf(edge -> edge.getTo().equals(to));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return Objects.equals(value, vertex.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
