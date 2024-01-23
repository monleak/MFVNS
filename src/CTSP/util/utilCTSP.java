package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;
import CTSP.benchmark.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

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

    /**
     * Kiểm tra xem 2 đỉnh có nằm cùng cluster hay không
     * @param graph Đồ thị đầu vào
     * @param v1 id đỉnh 1
     * @param v2 id đỉnh 2
     * @return
     */
    public static boolean isSameCluster(Graph graph, int v1, int v2){
        //v1,v2 cần +1 để ra id của đỉnh
        for (int i = 0; i < graph.listCluster.size(); i++) {
            var check1 = graph.listCluster.get(i).listIDVertex.contains(v1+1);
            var check2 = graph.listCluster.get(i).listIDVertex.contains(v2+1);
            if(check1 && check2){
                return true;
            }
            if(check1 || check2){
                return false;
            }
        }
        return false;
    }

    /**
     * Tính toán sự khác nhau trong gen giữa 2 cá thể
     * @param indiv1
     * @param indiv2
     * @return int
     */
    public static int calDif(Individual indiv1, Individual indiv2){
        int dif = 0;
        for (int i = 0; i < indiv1.Chromosome.length; i++) {
            int target = indiv1.Chromosome[i];
            int j = IntStream.range(0, indiv2.Chromosome.length)
                    .filter(n -> indiv2.Chromosome[n] == target)
                    .findFirst()
                    .orElse(-1);
            int prev_j = j-1;
            int successor_j = j+1;
            if(j==0){
                prev_j = indiv2.Chromosome.length-1;
            }
            if(j==indiv2.Chromosome.length-1){
                successor_j = 0;
            }
            if(!(indiv2.Chromosome[prev_j] == indiv1.Chromosome[i] || indiv2.Chromosome[successor_j] == indiv1.Chromosome[i])){
                dif++;
            }
        }
        return dif;
    }
}
