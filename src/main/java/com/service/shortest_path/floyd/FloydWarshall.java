package com.service.shortest_path.floyd;

import java.util.Map;

import com.service.shortest_path.DistanceCalculator;
import com.service.shortest_path.Vertex;

public class FloydWarshall {

    private static int noOfVertices = 0;
    private static int noOfEdges = 0;
    private static Graph graph = null;
    private static long[][][] resultList = null;
    private final DistanceCalculator distanceCalculator;
    private static Map<Integer, Vertex> adjacencyVertexMap = null;

    public FloydWarshall() {
        distanceCalculator = new DistanceCalculator();
    }


    //algorytm Floyda-Warshalla znajduje koszty dojścia pomiędzy
    //wszystkimi parami wierzchołków w grafie
    public void findShortestPath() {
        int[][] adjacencyList = graph.getAdjacencyList();
        int size = adjacencyList.length;
        resultList = new long[size][size][size];

        //inicjalizacja
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                resultList[0][i][j] = adjacencyList[i][j];
            }
        }

        //dla wszystkich par wierzchołku grafu modyfikujemy minimalny koszt
        for (int k = 1; k < size; k++) {
            for (int i = 1; i < size; i++) {
                for (int j = 1; j < size; j++) {
                    //badamy czy koszt dojścia d[i,j] jest większy od sumy kosztów dojść d[i,k] + d[k,j]
                    resultList[k][i][j] = getMinimumWeight(k, i, j);
                }
            }
        }
    }

    public long getMinimumWeight(int k, int i, int j) {
        return Math.min(getWeight(i, j), (getWeight(i, k) + getWeight(k, j)));
    }

    public long getWeight(int src, int dest) {
        Vertex start = adjacencyVertexMap.get(src);
        Vertex end = adjacencyVertexMap.get(dest);

        double srcDistance = start.getMinDistance();
        double targetDistance = end.getMinDistance();
        return (long) (srcDistance + targetDistance);
    }
}
