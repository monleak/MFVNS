package CTSP.benchmark;

import CTSP.basic.Individual;

import java.util.ArrayList;
import java.util.Comparator;

public class Cluster {
    public ArrayList<Integer> listIDVertex;
    public ArrayList<Vertex> listVertex;

    public Cluster(){
        listVertex = new ArrayList<>();
        listIDVertex = new ArrayList<>();
    }

    public void addVertex(Vertex v){
        listVertex.add(v);
        listIDVertex.add(v.id);
    }

    public void sortListVertex() {
        //Sắp xếp lại các đỉnh trong cluster
        this.listVertex.sort(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return Double.compare(o1.disToO, o2.disToO);
            }
        });
    }
}
