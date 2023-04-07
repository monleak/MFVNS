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

    /**
     * Mã hóa cá thể tử không gian riêng vào không gian chung
     * Lưu ý cần đảm bảo thứ tự di chuyển giữa các cluster
     * (Thứ tự di chuyển giữa các cluster được quyết định bằng trung bình cộng các nút trong cluster theo thứ tự giảm dần: Lớn hơn đi trước)
     * @param decodeChromosome
     * @param oldChromosome
     * @param NOVPCinCommonSpace
     * @param NOVPCinPrivateSpace
     * @return Gen sau khi được mã hóa
     */
    public static int[] encodeChromosome(int[] decodeChromosome, int[] oldChromosome, int[] NOVPCinCommonSpace, int[] NOVPCinPrivateSpace){
        
    }

    /**
     * Tính độ dài đường đi trong không gian riêng
     *
     * @param graph
     * @param Chromosome
     * @param NOVPCinCommonSpace
     * @return Độ dài đường đi
     */
    public static double calCost(Graph graph, int[] Chromosome, int[] NOVPCinCommonSpace){
        double cost = 0;
        int[] ClusterOrder = new int[graph.numberOfCluster];
        int[] NOVPCinPrivateSpace = new int[graph.numberOfCluster];
        for(int i=0;i<graph.numberOfCluster;i++){
            NOVPCinPrivateSpace[i] = graph.listCluster.get(i).listVertex.size();
        }
        int[] decodeChromosome = decodeChromosome(graph.totalVertices, Chromosome,NOVPCinCommonSpace,NOVPCinPrivateSpace);
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
                clusterSegment[j] = decodeChromosome[j+pointPrivateSpace[i]];
            }
            ClusterOrder[i] = Arrays.stream(clusterSegment).sum()/clusterSegment.length;
            clusterSegment = convertOrder(clusterSegment,0);
            listClusterSegment.add(clusterSegment);
            for(int j=0;j<clusterSegment.length-1;j++){
                totalCostInCluster[i] += graph.distance[graph.listCluster.get(i).listVertex.get(j).id-1][graph.listCluster.get(i).listVertex.get(j+1).id-1];
            }
        }
        //Thứ tự di chuyển giữa các cluster được sắp xếp dựa trên trung bình cộng clusterSegment
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

    /**
     * Hàm tính độ dài đường đi nhưng gen truyền vào là gen đã được mã hóa ra không gian riêng
     * @param graph
     * @param decodeChromosome
     * @param NOVPCinPrivateSpace
     * @return
     */
    public static double calCostWithoutDecode(Graph graph, int[] decodeChromosome, int[] NOVPCinPrivateSpace){
        double cost = 0;
        int[] ClusterOrder = new int[graph.numberOfCluster];

        //Tính độ dài đường đi trong các cluster
        double[] totalCostInCluster = new double[graph.numberOfCluster];
        int[] pointPrivateSpace = new int[NOVPCinPrivateSpace.length];
        for(int i=0;i<pointPrivateSpace.length;i++){
            pointPrivateSpace[i] = i == 0 ? 0 : (pointPrivateSpace[i-1]+NOVPCinPrivateSpace[i]);
        }
        //TODO:  nhớ convertOrder
        ArrayList<int[]> listClusterSegment = getListClusterSegment(graph.numberOfCluster,NOVPCinPrivateSpace,decodeChromosome,pointPrivateSpace);
        for(int i=0;i<listClusterSegment.size();i++){
            ClusterOrder[i] = Arrays.stream(listClusterSegment.get(i)).sum()/listClusterSegment.get(i).length;
            listClusterSegment.set(i, convertOrder(listClusterSegment.get(i),0));
            for(int j=0;j<listClusterSegment.get(i).length-1;j++){ //TODO: check
                totalCostInCluster[i] += graph.distance[graph.listCluster.get(i).listVertex.get(j).id-1][graph.listCluster.get(i).listVertex.get(j+1).id-1];
            }
        }
        // for(int i=0;i<graph.numberOfCluster;i++){
        //     int[] clusterSegment = new int[NOVPCinPrivateSpace[i]];
        //     for(int j=0;j<clusterSegment.length;j++){
        //         clusterSegment[j] = decodeChromosome[j+pointPrivateSpace[i]];
        //     }
        //     ClusterOrder[i] = Arrays.stream(clusterSegment).sum()/clusterSegment.length;
        //     clusterSegment = convertOrder(clusterSegment,0);
        //     listClusterSegment.add(clusterSegment);
        //     for(int j=0;j<clusterSegment.length-1;j++){
        //         totalCostInCluster[i] += graph.distance[graph.listCluster.get(i).listVertex.get(j).id-1][graph.listCluster.get(i).listVertex.get(j+1).id-1];
        //     }
        // }
        //Thứ tự di chuyển giữa các cluster được sắp xếp dựa trên trung bình cộng clusterSegment
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

    /**
     * Cắt các đoạn cluster ra từ NST ban đầu
     * @param numberOfCluster
     * @param NOVPCinPrivateSpace
     * @param decodeChromosome
     * @param pointPrivateSpace
     * @return 
     */
    public static ArrayList<int[]> getListClusterSegment(int numberOfCluster, int[] NOVPCinPrivateSpace, int[] decodeChromosome, int[] pointPrivateSpace){
        ArrayList<int[]> listClusterSegment = new ArrayList<>();
        for(int i=0;i<numberOfCluster;i++){
            int[] clusterSegment = new int[NOVPCinPrivateSpace[i]];
            for(int j=0;j<clusterSegment.length;j++){
                clusterSegment[j] = decodeChromosome[j+pointPrivateSpace[i]];
            }
            listClusterSegment.add(clusterSegment);
        }
        return listClusterSegment;
    }
}
