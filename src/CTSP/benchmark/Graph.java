package CTSP.benchmark;

import CTSP.basic.Individual;

import java.util.ArrayList;

public class Graph {
    public int totalVertices; //tổng số đỉnh
    public ArrayList<Vertex> vertexList; //danh sách đỉnh
    public double optimal; //Giá trị tối ưu

    public double[][] distance; //distance[i][j] = khoảng cách từ đỉnh i đến đỉnh j

    public int numberOfCluster; //Số cụm
    public int sourceVertex; //Đỉnh nguồn (Trong bài CTSP không được sử dụng đến)
    public String linkFile;

    public ArrayList<Integer>[] listCluster;

    public Graph(int totalVertices,int numberOfCluster, String linkFile){
        this.totalVertices = totalVertices;
        this.vertexList = new ArrayList<>();
        this.distance = new double[totalVertices][totalVertices];
        this.linkFile = linkFile;
        this.numberOfCluster = numberOfCluster;

        this.listCluster = new ArrayList[numberOfCluster];
        for(int i = 0 ;i<numberOfCluster;i++){
            listCluster[i] = new ArrayList<>();
        }
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
        this.listCluster[idCluster].add(idVertex);
    }

    /**
     * Sắp xếp lại các cluster theo thứ tự số lượng đỉnh tăng dần
     */
    public void sortListCluster(){
        //TODO: cần check
        for (int i = 0; i < numberOfCluster-1; i++){
            // i phần tử cuối cùng đã được sắp xếp
            boolean haveSwap = false;
            for (int j = 0; j < numberOfCluster-i-1; j++){
                if (this.listCluster[j].size() > this.listCluster[j+1].size()){
                    ArrayList<Integer> temp = this.listCluster[j];
                    this.listCluster[j] = this.listCluster[j+1];
                    this.listCluster[j+1] = temp;

                    haveSwap = true; // Kiểm tra lần lặp này có swap không
                }
            }
            // Nếu không có swap nào được thực hiện => mảng đã sắp xếp. Không cần lặp thêm
            if(haveSwap == false){
                break;
            }
        }

    }
}
