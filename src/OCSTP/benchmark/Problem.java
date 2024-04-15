package OCSTP.benchmark;

import OCSTP.basic.Params;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static OCSTP.IO.ReadDataOCSTP.scanOCSTPfile;

/**
 * Mỗi instance là 1 problem
 * pathInstance là đường dẫn đến thư mục chứa các file .clt
 * VD: Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large
 */
public class Problem {
    public ArrayList<Graph> graphs;
    public int maxTotalVertices;
    public Problem(String pathInstance,ArrayList<String> orderTask){
        graphs = new ArrayList<>();
        graphs.addAll(scanOCSTPfile(pathInstance,orderTask));

        this.maxTotalVertices = 0;
        for(var temp : this.graphs){
            if (this.maxTotalVertices < temp.totalVertices){
                this.maxTotalVertices = temp.totalVertices;
            }
        }
    }
}
