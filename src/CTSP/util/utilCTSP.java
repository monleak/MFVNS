package CTSP.util;

import CTSP.basic.Params;
import CTSP.benchmark.Graph;

import java.util.ArrayList;
import java.util.Arrays;

import static CTSP.util.util.convertOrder;
import static CTSP.util.util.convertOrder2;

public class utilCTSP {
    /**
     * Giải mã cá thể từ không gian chung ra không gian riêng
     * @param totalVertices Tổng số lượng đỉnh trong không gian riêng
     * @param Chromosome Gen ban đầu nằm trong không gian chung
     * @param NOVPCinCommonSpace Số lượng đỉnh từng cluster trong không gian chung
     * @param NOVPCinPrivateSpace Số lượng đỉnh từng cluster trong không gian riêng
     * @return NST sau khi giải mã
     */
    public static int[] decodeChromosome(int totalVertices, int[] Chromosome,
                                         int[] NOVPCinCommonSpace,
                                         int[] NOVPCinPrivateSpace){
        int[] pointCommonSpace = new int[NOVPCinCommonSpace.length];
        for(int i=0;i<pointCommonSpace.length;i++){
            pointCommonSpace[i] = i == 0 ? 0 : (pointCommonSpace[i-1]+NOVPCinCommonSpace[i]);
        }
        int[] pointPrivateSpace = new int[NOVPCinPrivateSpace.length];
        for(int i=0;i<pointPrivateSpace.length;i++){
            pointPrivateSpace[i] = i == 0 ? 0 : (pointPrivateSpace[i-1]+NOVPCinPrivateSpace[i]);
        }

        int[] decodeChromosome = new int[totalVertices];
        for(int i=0;i<NOVPCinPrivateSpace.length;i++){
            //Mã hóa từng cluster
            int pointDecode = pointPrivateSpace[i];
            int pointChromo = pointCommonSpace[i];
            while (pointDecode<NOVPCinPrivateSpace[i]){
                decodeChromosome[pointDecode] = Chromosome[pointChromo];
                pointDecode++;pointChromo++;
            }
        }
        return decodeChromosome;
    }


    public static void encodeChromosome(){

    }

    /**
     * Tính độ dài đường đi trong không gian riêng
     * @param graph
     * @param Chromosome NST trong không gian chung
     * @param NOVPCinCommonSpace Số lượng đỉnh mỗi cluster trong không gian chung
     * @return Độ dài đường đi
     */
    public static double calCost(Graph graph, int[] Chromosome, int[] NOVPCinCommonSpace){
        double cost = 0;
        int[] ClusterOrder = new int[graph.numberOfCluster];
        int[] NOVPCinPrivateSpace = new int[graph.numberOfCluster];
        for(int i=0;i<graph.numberOfCluster;i++){
            NOVPCinPrivateSpace[i] = graph.listCluster.get(i).listVertex.size();
        }
        int[] decodeChromose = decodeChromosome(graph.totalVertices, Chromosome, NOVPCinCommonSpace, NOVPCinPrivateSpace);

        //Tính độ dài đường đi trong các cluster
        double[] totalCostInCluster = new double[graph.numberOfCluster];
        int[] pointPrivateSpace = new int[NOVPCinPrivateSpace.length];
        for(int i=0;i<pointPrivateSpace.length;i++){
            pointPrivateSpace[i] = i == 0 ? 0 : (pointPrivateSpace[i-1]+NOVPCinPrivateSpace[i]);
        }
        ArrayList<int[]> listClusterSegment = new ArrayList<>();
        for(int i=0;i<graph.numberOfCluster;i++){
            int[] clusterSegment = new int[NOVPCinPrivateSpace[i]];
            for(int j=0;j<clusterSegment.length;j++){
                clusterSegment[j] = decodeChromose[j+pointPrivateSpace[i]];
            }
            clusterSegment = convertOrder(clusterSegment,0);
            listClusterSegment.add(clusterSegment);
            for(int j=0;j<clusterSegment.length-1;j++){
                totalCostInCluster[i] += graph.distance[graph.listCluster.get(i).listVertex.get(j).id-1][graph.listCluster.get(i).listVertex.get(j+1).id-1];
            }
            ClusterOrder[i] = Arrays.stream(clusterSegment).sum();
        }
        ClusterOrder = convertOrder2(ClusterOrder);
        //Tổng giá trị đường đi = tổng độ dài đường đi trong cluster + tổng độ dài đoạn nối các cluster
        //Tính tổng độ dài các đoạn nối
        double totalCostLink = 0;
        for(int i=0;i<graph.numberOfCluster-1;i++){
            totalCostLink += graph.distance
                            [graph.listCluster.get(ClusterOrder[i]).listVertex.get(listClusterSegment.get(ClusterOrder[i])[listClusterSegment.get(ClusterOrder[i]).length-1]).id-1]
                            [graph.listCluster.get(ClusterOrder[i+1]).listVertex.get(listClusterSegment.get(ClusterOrder[i+1])[listClusterSegment.get(ClusterOrder[i+1]).length-1]).id-1];
        }
        totalCostLink += graph.distance
                [graph.listCluster.get(ClusterOrder[graph.numberOfCluster-1]).listVertex.get(listClusterSegment.get(ClusterOrder[graph.numberOfCluster-1])[listClusterSegment.get(ClusterOrder[graph.numberOfCluster-1]).length-1]).id-1]
                [graph.listCluster.get(ClusterOrder[0]).listVertex.get(listClusterSegment.get(ClusterOrder[0])[listClusterSegment.get(ClusterOrder[0]).length-1]).id-1];

        for(double temp : totalCostInCluster){
            cost+=temp;
        }
        cost+=totalCostLink;
        Params.countEvals++;
        return cost;
    }
}
