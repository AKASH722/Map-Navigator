package co.akash.mapnavigator.util;

import co.akash.mapnavigator.model.BusStation;
import co.akash.mapnavigator.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphUtils {


    public Graph buildGraph(List<BusStation> busStations, List<Route> routes) {
        Graph graph = new Graph();
        for (BusStation busStation : busStations) {
            graph.addVertex(busStation.getName().toLowerCase());
        }
        for (Route route : routes) {
            graph.addEdge(graph.getVertex(route.getFromStation().getName().toLowerCase()), graph.getVertex(route.getToStation().getName().toLowerCase()), route.getDistance());
        }
        return graph;
    }

    public ArrayList<?> dijkstra(Graph graph, Vertex startingVertex) {
        Dictionary<String, Integer> distances = new Hashtable<>();
        Dictionary<String, Vertex> previous = new Hashtable<>();
        PriorityQueue<Pair<Vertex, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));

        queue.add(new Pair<>(startingVertex, 0));

        for (Vertex vertex : graph.getVertices()) {
            distances.put(vertex.getValue(), Integer.MAX_VALUE);
            previous.put(vertex.getValue(), new Vertex(null));
        }
        distances.put(startingVertex.getValue(), 0);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll().getKey();
            for (Edge edge : current.getEdges()) {
                Integer currentDistance = distances.get(edge.getTo().getValue());
                Integer alternativeDistance = distances.get(current.getValue()) + edge.getWeight();

                if (alternativeDistance < currentDistance) {
                    distances.put(edge.getTo().getValue(), alternativeDistance);
                    previous.put(edge.getTo().getValue(), current);
                    queue.add(new Pair<>(edge.getTo(), alternativeDistance));
                }
            }
        }

        return new ArrayList<>(Arrays.asList(distances, previous));
    }

    public Pair<ArrayList<Vertex>, Integer> shortestPathBetween(Graph graph, Vertex startingVertex, Vertex targetVertex) {
        ArrayList<?> dijkstraDictionaries = dijkstra(graph, startingVertex);
        Dictionary<String, Integer> distances = (Dictionary<String, Integer>) dijkstraDictionaries.get(0);
        Dictionary<String, Vertex> previous = (Dictionary<String, Vertex>) dijkstraDictionaries.get(1);
        ArrayList<Vertex> path = new ArrayList<>();
        Vertex vertex = targetVertex;
        while (vertex.getValue() != null) {
            path.addFirst(vertex);
            vertex = previous.get(vertex.getValue());
        }
        return new Pair<>(path, distances.get(targetVertex.getValue()));
    }

    @Data
    @AllArgsConstructor
    public static class Pair<K, V> {
        public K Key;
        public V Value;
    }
}
