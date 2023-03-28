package CTSP.benchmark;

import java.util.ArrayList;

public class Graph {
    public int totalVertices; //tổng số đỉnh
    public ArrayList<Vertex> vertexList; //danh sách đỉnh
    public double optimal;

    public double[][] distance; //distance[i][j] = khoảng cách từ đỉnh i đến đỉnh j

    public int numberOfCluster; //Số cụm
    public int sourceVertex;
    public String linkFile;

    public Graph(int totalVertices, String linkFile){
        this.totalVertices = totalVertices;
        this.vertexList = new ArrayList<>();
        this.distance = new double[totalVertices][totalVertices];
        this.linkFile = linkFile;
    }

    public void addVertex(Vertex a){
        this.vertexList.add(a);
    }

    public void setOptimal(double optimal){
        this.optimal = optimal;
    }
}
