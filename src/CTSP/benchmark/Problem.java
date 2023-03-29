package CTSP.benchmark;

import CTSP.basic.Params;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static CTSP.IO.DataIO.readDataCTSP;
import static CTSP.IO.ReadDataCTSP.scanCTSPfile;

/**
 * Mỗi instance là 1 problem
 * pathInstance là đường dẫn đến thư mục chứa các file .clt
 * VD: Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large
 */
public class Problem {
    public ArrayList<Graph> graphs;
    public int numberOfGraph;
    public int maxTotalVertices;
    public String instanceName;

    public Problem(String pathInstance){
        graphs = new ArrayList<>();
        graphs.addAll(scanCTSPfile(pathInstance));
        this.instanceName = Paths.get(pathInstance).getFileName().toString();
        this.numberOfGraph = graphs.size();
        for(Graph g: graphs){
            if(this.maxTotalVertices < g.totalVertices)
                this.maxTotalVertices = g.totalVertices;
        }
    }
}
