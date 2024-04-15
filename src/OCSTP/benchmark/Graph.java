package OCSTP.benchmark;

import OCSTP.basic.Individual;

import java.util.ArrayList;
import java.util.Comparator;

import static OCSTP.util.util.calDis;

public class Graph {
    public int totalVertices; //tổng số đỉnh
    public double optimal; //Giá trị tối ưu
    public double[][] distance; //distance[i][j] = khoảng cách từ đỉnh i đến đỉnh j
    public double[][] reqs; //reqs[i][j] = giá trị reqs giữa 2 đinh i j
    public String linkFile;
    public Graph(int totalVertices, String linkFile){
        this.totalVertices = totalVertices;
        distance = new double[totalVertices][totalVertices];
        reqs = new double[totalVertices][totalVertices];
        this.linkFile = linkFile;
    }
}
