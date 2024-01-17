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
     * Tính thông qua distance_with_Penalize_Edge sau đó trừ đi số lượng cạnh liên cụm được phép
     *
     * @param graph
     * @param Chromosome
     * @return Độ dài đường đi
     */
    public static double calCost(Graph graph, int[] Chromosome) {
        if(graph.distance_with_Penalize_Edge == null){
            graph.cal_distance_with_Penalize_Edge();
        }
        double cost = 0;

        for(int i = 0; i<Chromosome.length-1;i++){
            cost += graph.distance_with_Penalize_Edge[Chromosome[i]][Chromosome[i+1]];
        }
        cost += graph.distance_with_Penalize_Edge[Chromosome[Chromosome.length-1]][Chromosome[0]];
        Params.countEvals++;
        return cost - graph.numberOfCluster*graph.Penalize_Edge;
    }

    /**
     * Tính cost của 1 đoạn đường có phạt cạnh
     * @param graph
     * @param Chromosome có thể chỉ chứa 1 phần NST
     * @return
     */
    public static double calCost_with_Penalize_Edge(Graph graph, int[] Chromosome){
        if(graph.distance_with_Penalize_Edge == null){
            graph.cal_distance_with_Penalize_Edge();
        }
        double cost = 0;

        for(int i = 0; i<Chromosome.length-1;i++){
            cost += graph.distance_with_Penalize_Edge[Chromosome[i]][Chromosome[i+1]];
        }
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
