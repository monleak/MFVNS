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
     * @param totalVertexPrivateSpace Tổng số lượng đỉnh trong không gian riêng
     * @param Chromosome Gen ban đầu nằm trong không gian chung
     * @param pointCommonSpace
     * @param pointPrivateSpace
     * @param NOVPCinPrivateSpace Số lượng đỉnh từng cluster trong không gian riêng
     * @return NST sau khi giải mã
     */
    public static int[] decodeChromosome(int totalVertexPrivateSpace, int[] Chromosome,
                                         int[] pointCommonSpace,
                                         int[] NOVPCinPrivateSpace,
                                         int[] pointPrivateSpace){
        int[] decodeChromosome = new int[totalVertexPrivateSpace];
        for(int i=0;i<NOVPCinPrivateSpace.length;i++){
            //Mã hóa từng cluster
            int pointDecode = pointPrivateSpace[i];
            int pointChromo = pointCommonSpace[i];
            while(pointDecode<NOVPCinPrivateSpace[i]+pointPrivateSpace[i]){
                decodeChromosome[pointDecode++] = Chromosome[pointChromo++];
            }
        }
        return decodeChromosome;
    }

    /**
     * Mã hóa cá thể tử không gian riêng vào không gian chung
     * @param decodeChromosome
     * @param oldChromosome
     * @param NOVPCinCommonSpace
     * @param NOVPCinPrivateSpace
     * @return Gen sau khi được mã hóa
     */
    public static int[] encodeChromosome(int[] decodeChromosome, int[] oldChromosome, int[] NOVPCinCommonSpace, int[] NOVPCinPrivateSpace, int[] pointPrivateSpace){
        int[] encodeChromosome = new int[oldChromosome.length];
        int pointDecode =0, pointEncode = 0;
        for(int i=0;i<NOVPCinCommonSpace.length;i++){
            for(int j=0;j<NOVPCinCommonSpace[i];j++){
                if(i<NOVPCinPrivateSpace.length){
                    if(pointDecode<NOVPCinPrivateSpace[i]+pointPrivateSpace[i]){
                        encodeChromosome[pointEncode++] = decodeChromosome[pointDecode++];
                    }else {
                        encodeChromosome[pointEncode] = oldChromosome[pointEncode];
                        pointEncode++;
                    }
                }else {
                    encodeChromosome[pointEncode] = oldChromosome[pointEncode];
                    pointEncode++;
                }
            }
        }
        return encodeChromosome;
    }

    /**
     * Tính độ dài đường đi trong không gian riêng
     * (Thứ tự di chuyển giữa các cluster được quyết định bằng trung bình cộng các nút trong cluster theo thứ tự giảm dần: Lớn hơn đi trước)
     * Thứ tự di chuyển giữa cluster sẽ được tính sau khi decode
     *
     * @param graph
     * @param Chromosome
     * @param pointCommonSpace
     * @param type 1-Chromosome thuộc không gian chung, 2-Chromosome thuộc KG riêng
     * @return Độ dài đường đi
     */
    public static double calCost(Graph graph, int[] Chromosome, int type, int[] pointCommonSpace){
        double cost = 0;
        int[] ClusterOrder = new int[graph.numberOfCluster];
        int[] NOVPCinPrivateSpace = graph.NOVPCinPrivateSpace;
        int[] decodeChromosome = null;
        if(type == 1){
            decodeChromosome = decodeChromosome(graph.totalVertices, Chromosome,pointCommonSpace,NOVPCinPrivateSpace, graph.pointPrivateSpace);
        }else if(type == 2){
            decodeChromosome = Chromosome.clone(); 
        }else {
            System.exit(9999);
        }
        //Tính độ dài đường đi trong các cluster
        double[] totalCostInCluster = new double[graph.numberOfCluster];
        int[] pointPrivateSpace = graph.pointPrivateSpace;
        ArrayList<int[]> listClusterSegment = new ArrayList<>();
        for(int i=0;i<graph.numberOfCluster;i++){
            int[] ClusterSegment = getClusterSegment(decodeChromosome,pointPrivateSpace[i],NOVPCinPrivateSpace[i]);
            //TODO: kiểm tra lại ClusterOrder
            ClusterOrder[i] = Arrays.stream(ClusterOrder).sum()/ClusterSegment.length;
            ClusterSegment = convertOrder(ClusterSegment,0);
            listClusterSegment.add(ClusterSegment);
            for(int j=0;j<ClusterSegment.length-1;j++){
                totalCostInCluster[i] += graph.distance[graph.listCluster.get(i).listVertex.get(ClusterSegment[j]).id-1]
                                                       [graph.listCluster.get(i).listVertex.get(ClusterSegment[j+1]).id-1];
            }
        }
        ClusterOrder = convertOrder2(ClusterOrder);

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

        //Tổng giá trị đường đi = tổng độ dài đường đi trong cluster + tổng độ dài đoạn nối các cluster
        for(double temp : totalCostInCluster){
            cost+=temp;
        }
        cost+=totalCostLink;
        Params.countEvals++;
        return cost;
    }

    /**
     * @param point Mảng chứa các điểm bắt đầu của các cluster
     * @param i
     * @return Trả về điểm i thuộc cluster bao nhiêu
     */
    public static int inCluster(int[] point,int i){
        for(int p=0;p<point.length-1;p++){
            if(point[p]<=i && point[p+1] > i)
                return p;
        }
        return point.length-1;
    }

    /**
     * Cắt đoạn cluster ra từ nhiễm sắc thể
     * @param Chromosome
     * @param pointStart
     * @param numberOfVertex
     * @return
     */
    public static int[] getClusterSegment(int[] Chromosome, int pointStart, int numberOfVertex){
        int[] ClusterSegment = new int[numberOfVertex];
        int j = 0;
        for (int i=pointStart;i<numberOfVertex+pointStart;i++){
            ClusterSegment[j++] = Chromosome[i];
        }
        return ClusterSegment;
    }
}
