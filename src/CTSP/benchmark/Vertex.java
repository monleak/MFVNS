package CTSP.benchmark;

public class Vertex {
    public int id;
    public int x;
    public int y;
    public int idCluster;

    public Vertex(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Vertex(int id){
        this.id = id;
    }
    public void addCluster(int idCluster){
        this.idCluster = idCluster;
    }
}
