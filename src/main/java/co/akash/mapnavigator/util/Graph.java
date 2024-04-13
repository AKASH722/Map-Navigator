package co.akash.mapnavigator.util;

import lombok.Data;

import java.util.ArrayList;


@Data
public class Graph {
    private final ArrayList<Vertex> vertices;

    public Graph() {
        this.vertices = new ArrayList<>();
    }

    public boolean addVertex(String value) {
        Vertex newVertex = new Vertex(value);
        for (Vertex vertex : vertices) {
            if (vertex.equals(newVertex)) {
                return false;
            }
        }
        vertices.add(newVertex);
        return true;
    }

    public boolean addEdge(Vertex from, Vertex to, Integer weight) {
        if (vertices.contains(from) && vertices.contains(to)) {
            return from.addEdge(to, weight);
        }
        return false;
    }

    public Vertex getVertex(String value) {
        for (Vertex vertex : vertices) {
            if (vertex.getValue().equals(value)) {
                return vertex;
            }
        }
        return null;
    }

    public boolean removeVertex(String value) {
        for (Vertex vertex : vertices) {
            if (vertex.getValue().equals(value)) {
                removeFromEdges(vertex);
                return vertices.remove(vertex);
            }
        }
        return false;
    }

    private void removeFromEdges(Vertex toRemove) {
        for (Vertex vertex : vertices) {
            if (!vertex.equals(toRemove)) {
                removeEdge(vertex, toRemove);
            }
        }
    }


    public boolean removeEdge(Vertex from, Vertex to) {
        if (vertices.contains(from) && vertices.contains(to)) {
            from.removeEdge(to);
            return true;
        }
        return false;
    }
}

