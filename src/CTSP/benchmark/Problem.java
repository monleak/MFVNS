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

    /**
     * numberOfVerticesPerCluster là số lượng đỉnh lớn nhất ở mỗi cluster.
     * Ví dụ:
     * Graph 1 có 4 cluster lần lượt là: C1 có 3 đỉnh, C2 có 5 đỉnh, C3 có 1 đỉnh, C4 có 2 đỉnh
     * Graph 2 có 3 cluster lần lượt là: C1 có 3 đỉnh, C2 có 2 đỉnh, C3 có 4 đỉnh
     * Thì numberOfVerticesPerCluster[] = {3,5,4,2}
     */
    public int[] numberOfVerticesPerCluster;
    public int maxNumberOfCluster;
    public String instanceName; //Tên của folder chứa các file .clt

    public Problem(String pathInstance){
        //TODO: cần check
        graphs = new ArrayList<>();
        graphs.addAll(scanCTSPfile(pathInstance));
        this.instanceName = Paths.get(pathInstance).getFileName().toString();
        this.numberOfGraph = graphs.size();

        for(int i=0;i<numberOfGraph;i++){
            graphs.get(i).sortListCluster();
            if(maxNumberOfCluster < graphs.get(i).numberOfCluster){
                maxNumberOfCluster = graphs.get(i).numberOfCluster;
            }
        }

        this.numberOfVerticesPerCluster = maxNumberOfCluster != 0 ? new int[maxNumberOfCluster] : null;
        for(Graph g : graphs){
            for(int i = 0;i<maxNumberOfCluster;i++){
                if(numberOfVerticesPerCluster[i] < g.listCluster[i].size()){
                    numberOfVerticesPerCluster[i] = g.listCluster[i].size();
                }
            }
        }
    }
}
