package com.service.shortest_path.floyd;
/**
 * 
 */

import java.util.List;
import java.util.Map;

import com.domain.Flight;
import com.service.shortest_path.DistanceCalculator;
import com.service.shortest_path.Edge;
import com.service.shortest_path.Vertex;


public class Graph {

	public static int[][] adjacencyMatrix=null; 
	public static int noOfVertices=0;
	public static int noOfEdges=0;
	private static Map<Vertex, List<Edge>> adjacencyVertexMap = null;
	private final DistanceCalculator distanceCalculator;

	public Graph(int noOfVertices)
	{
		adjacencyMatrix=new int[noOfVertices+1][noOfVertices+1]; //+1 - because vertices are numbered from 1 to n (Not from 0)
		this.noOfVertices=noOfVertices;
		distanceCalculator = new DistanceCalculator();
	}

	public long getWeight(Flight f) {
		return distanceCalculator.distance(f.getFrom().getLatitude(), f.getTo().getLatitude(), f.getFrom().getLongitude(), f.getTo().getLongitude());
	}
	/**
	 * Method to add edge to the adjacency matrix
	 * @param u
	 * @param v
	 * @param w
	 */
	public void addEdge(int u, int v, int w)
	{
		adjacencyMatrix[u][v]=w;
	}


	/**
	 * Method to re-align adjacency matrix by adding infinity value to non reachable vertices
	 */
	public void alignMatrix()
	{
		int rows=adjacencyMatrix.length;
		int columns=adjacencyMatrix[0].length;
		for(int i=1;i<rows;i++)
		{
			for(int j=1;j<columns;j++)
			{
				if(i!=j)
				{
					if(adjacencyMatrix[i][j]==0)
					{
						adjacencyMatrix[i][j]=Integer.MAX_VALUE;
					}
				}
			}
		}
	}
	
	/**
	 * Method to return the adjacency matrix
	 * @return
	 */
	public int[][] getAdjacencyList()
	{
		return adjacencyMatrix;
	}


	/**
	 * Method to print the graph
	 */
	public void printGraph()
	{
		int rows=adjacencyMatrix.length;
		int columns=adjacencyMatrix[0].length;
		for(int i=1;i<rows;i++)
		{
			for(int j=1;j<columns;j++)
			{
				System.out.print("  "+adjacencyMatrix[i][j]);
			}
			System.out.println();
		}
	}


	public static void main(String[] args) {

		Graph graph=new Graph(5);
		graph.addEdge(1, 2, 5);
		graph.addEdge(1, 3, 12);
		graph.addEdge(3, 1, 12);
		graph.addEdge(2, 1, 5);
		graph.addEdge(2, 3, 7);
		graph.addEdge(3, 2, 7);
		
		graph.addEdge(4, 2, 9);
		graph.addEdge(5, 4, 10);
		graph.addEdge(5, 2, 11);

		graph.alignMatrix();
		graph.printGraph();
	}

}
