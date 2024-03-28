package OCSTP.benchmark;

import OCSTP.basic.Individual;

import java.util.ArrayList;
import java.util.Comparator;

import static OCSTP.util.util.calDis;

public class Graph {
    public int totalVertices; //tổng số đỉnh
    public ArrayList<Vertex> vertexList; //danh sách đỉnh
    public double optimal; //Giá trị tối ưu
    public double[][] distance; //distance[i][j] = khoảng cách từ đỉnh i đến đỉnh j
    public int numberOfCluster; //Số cụm
    public int sourceVertex; //Đỉnh nguồn (Trong bài CTSP không được sử dụng đến)
    public String linkFile;

    public ArrayList<Cluster> listCluster; //Danh sách các cluster
    public int[] NOVPCinPrivateSpace; //Số lượng đỉnh mỗi cluster
    public int[] pointPrivateSpace; //Điểm bắt đầu mỗi cluster

    public Graph(int totalVertices,int numberOfCluster, String linkFile){
        this.totalVertices = totalVertices;
        this.vertexList = new ArrayList<>();
        this.distance = new double[totalVertices][totalVertices];
        this.linkFile = linkFile;
        this.numberOfCluster = numberOfCluster;
        this.optimal = -1;
        this.listCluster = new ArrayList<>();
        for(int i=0;i<numberOfCluster;i++){
            listCluster.add(new Cluster());
        }
        this.NOVPCinPrivateSpace = new int[numberOfCluster];
        this.pointPrivateSpace = new int[numberOfCluster];
    }

    public void addVertex(Vertex a){
        this.vertexList.add(a);
    }

    /**
     * Thêm đỉnh vào 1 cụm
     * @param idCluster
     * @param idVertex
     */
    public void addVertexToCluster(int idCluster, int idVertex){
        this.listCluster.get(idCluster).addVertex(vertexList.get(idVertex));
    }

    /**
     * Sắp xếp lại các cluster theo thứ tự số lượng đỉnh tăng dần
     */
    public void sortListCluster(){
        this.listCluster.sort(new Comparator<Cluster>() {
            @Override
            public int compare(Cluster o1, Cluster o2) {
                return Integer.compare(o1.listVertex.size(), o2.listVertex.size());
            }
        });

    }
    public void setOptimal(int optimal){
        this.optimal = optimal;
    }

    /**
     * Tính NOVPCinPrivateSpace và pointPrivateSpace
     */
    public void calPrivateSpace(){
        for(int i=0;i<this.numberOfCluster;i++){
            this.NOVPCinPrivateSpace[i] = this.listCluster.get(i).listVertex.size();
            if(i==0){
                this.pointPrivateSpace[i] = 0;
            }else {
                this.pointPrivateSpace[i] = this.pointPrivateSpace[i-1]+this.NOVPCinPrivateSpace[i-1];
            }
        }
    }
}
