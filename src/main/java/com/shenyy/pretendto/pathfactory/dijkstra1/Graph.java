package com.shenyy.pretendto.pathfactory.dijkstra1;

import java.util.*;

class Graph {

    private final Map<Character, List<Vertex>> vertices;

    public Graph() {
        this.vertices = new HashMap<Character, List<Vertex>>();
    }

    public void addVertex(Character character, List<Vertex> vertex) {
        this.vertices.put(character, vertex);
    }

    public List<Character> getShortestPath(Character start, Character finish) {
        final Map<Character, Integer> distances = new HashMap<Character, Integer>();
        final Map<Character, Vertex> previous = new HashMap<Character, Vertex>();
        PriorityQueue<Vertex> nodes = new PriorityQueue<Vertex>();

        for(Character vertex : vertices.keySet()) {
            if (vertex == start) {
                distances.put(vertex, 0);
                nodes.add(new Vertex(vertex, 0));
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
            }
            previous.put(vertex, null);
        }

        while (!nodes.isEmpty()) {
            Vertex smallest = nodes.poll();
            if (smallest.getId() == finish) {
                final List<Character> path = new ArrayList<Character>();
                while (previous.get(smallest.getId()) != null) {
                    path.add(smallest.getId());
                    smallest = previous.get(smallest.getId());
                }
                return path;
            }

            if (distances.get(smallest.getId()) == Integer.MAX_VALUE) {
                break;
            }

            for (Vertex neighbor : vertices.get(smallest.getId())) {
                Integer alt = distances.get(smallest.getId()) + neighbor.getDistance();
                if (alt < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), alt);
                    previous.put(neighbor.getId(), smallest);

                    forloop:
                    for(Vertex n : nodes) {
                        if (n.getId() == neighbor.getId()) {
                            nodes.remove(n);
                            n.setDistance(alt);
                            nodes.add(n);
                            break forloop;
                        }
                    }
                }
            }
        }

        return new ArrayList<Character>(distances.keySet());
    }

}
