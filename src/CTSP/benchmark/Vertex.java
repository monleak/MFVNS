package CTSP.benchmark;

import CTSP.basic.Individual;

import static CTSP.util.util.calDis;

public class Vertex{
    public int id;
    public int x;
    public int y;
    public double disToO; //khoảng cách đến gốc tọa độ

    public Vertex(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.disToO = calDis(x,y,0,0);
    }

}
