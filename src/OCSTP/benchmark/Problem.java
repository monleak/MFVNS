package OCSTP.benchmark;

import OCSTP.basic.Params;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static OCSTP.IO.DataIO.readDataCTSP;
import static OCSTP.IO.ReadDataCTSP.scanCTSPfile;

/**
 * Mỗi instance là 1 problem
 * pathInstance là đường dẫn đến thư mục chứa các file .clt
 * VD: Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large
 */
public class Problem {
    public ArrayList<Graph> graphs;
    public int numberOfGraph;

    /**
     * NOVPCinCommonSpace là số lượng đỉnh lớn nhất ở mỗi cluster. (Có thể được viết tắt là NOVPC)
     * Ví dụ:
     * Graph 1 có 4 cluster lần lượt là: C1 có 3 đỉnh, C2 có 5 đỉnh, C3 có 1 đỉnh, C4 có 2 đỉnh
     * Graph 2 có 3 cluster lần lượt là: C1 có 3 đỉnh, C2 có 2 đỉnh, C3 có 4 đỉnh
     * Thì NOVPCinCommonSpace[] = {3,5,4,2}
     */
    public int[] NOVPCinCommonSpace;
    public int[] pointCommonSpace;
    public int maxNumberOfCluster;
    public String instanceName; //Tên của folder chứa các file .clt
    public int maxTotalVertices;

    public Problem(String pathInstance,ArrayList<String> orderTask){
        graphs = new ArrayList<>();
        graphs.addAll(scanCTSPfile(pathInstance,orderTask));
        this.instanceName = Paths.get(pathInstance).getFileName().toString();
        this.numberOfGraph = graphs.size();

        for(int i=0;i<numberOfGraph;i++){
            if(maxNumberOfCluster < graphs.get(i).numberOfCluster){
                maxNumberOfCluster = graphs.get(i).numberOfCluster;
            }
        }

        this.NOVPCinCommonSpace = new int[maxNumberOfCluster];
        for(Graph g : graphs){
            for(int i = 0;i<g.numberOfCluster;i++){
                if(NOVPCinCommonSpace[i] < g.listCluster.get(i).listVertex.size()){
                    NOVPCinCommonSpace[i] = g.listCluster.get(i).listVertex.size();
                }
            }
        }

        this.pointCommonSpace = new int[this.maxNumberOfCluster];
        for(int i=0;i<maxNumberOfCluster;i++){
            if(i==0){
                pointCommonSpace[i] = 0;
            }else{
                pointCommonSpace[i] = pointCommonSpace[i-1]+NOVPCinCommonSpace[i-1];
            }
        }
        this.maxTotalVertices =0;
        for(int temp : NOVPCinCommonSpace){
            this.maxTotalVertices+=temp;
        }
    }
}
