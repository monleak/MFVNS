package benchmark;

import java.util.ArrayList;

public class Graph {
    public int totalVertices;
    public ArrayList<Vertex> vertexList;
    public double optimal;

    public double[][] distance; //distance[i][j] = khoảng cách từ đỉnh i đến đỉnh j

    public Graph(int totalVertices){
        this.totalVertices = totalVertices;
        this.vertexList = new ArrayList<>();
        this.distance = new double[totalVertices][totalVertices];
    }

    public void addVertex(Vertex a){
        this.vertexList.add(a);
    }

    public void setOptimal(double optimal){
        this.optimal = optimal;
    }
}
