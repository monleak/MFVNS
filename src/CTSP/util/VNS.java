package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;
import CTSP.benchmark.Graph;

import java.util.Arrays;

import static CTSP.util.utilCTSP.*;

public class VNS {
    /**
     * Sử dụng double bridge để biến đổi gen ban đầu
     *
     * @param  Chromosome Gen ban đầu
     * @return Gen sau khi biến đổi
     */
    public static int[] Shaking(int[] Chromosome){
        int[] x = Chromosome.clone();
        if(Chromosome.length < 8){
            int p1,p2;
            p1 = Params.rand.nextInt(Chromosome.length);
            do{
                p2 = Params.rand.nextInt(Chromosome.length);
            }while (p2==p1);

            int temp = x[p1];
            x[p1] = x[p2];
            x[p2] = temp;
        } else {
            //Using double bridge
            int[] allowSelect = new int[x.length];
            for (int i=0;i<x.length;i++){
                allowSelect[i] = i;
            }
            int[] point = new int[4];
            for (int i=0;i<point.length;i++){
                int select;
                do{
                    select = Params.rand.nextInt(allowSelect.length);
                }while (allowSelect[select] == -1);

                point[i] = allowSelect[select];
                allowSelect[select] = -1;
                //select + 1
                if(select == x.length-1)
                    allowSelect[0] = -1;
                else
                    allowSelect[select+1] = -1;

                //select - 1
                if(select == 0)
                    allowSelect[x.length - 1] =-1;
                else
                    allowSelect[select - 1] = -1;
            }
            Arrays.sort(point);

            int[] tempX = new int[x.length];
            int count = 0;
            tempX[count++] = x[point[0]];
            for (int i = point[2]+1;i<=point[3];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[1]+1;i<=point[2];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[0]+1;i<=point[1];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for(int i=point[3]+1;i<x.length;i++){
                tempX[count++] = x[i];
            }
            for(int i=0;i<point[0];i++){
                tempX[count++] = x[i];
            }

            x = tempX;
        }
        return x;
    }

    /**
     * Di chuyển cá thể hiện tại tới lời giải tốt nhất trong tập các lời giải lân cận
     *
     * @param indiv Cá thể ban đầu
     * @param type Loại localSearch lựa chọn (1 - swap, 2 - 2opt)
     * @param graph Đồ thị cần trùng với skillfactor của cá thể
     * @param NOVPCinCommonSpace
     * @return boolean Có cải thiện sau khi search hay không (T/F)
     */
    public static boolean localSearch(Individual indiv, int type, Graph graph, int[] NOVPCinCommonSpace){
        boolean positive = false;
        //Shaking
        int[] NOVPCinPrivateSpace = new int[graph.numberOfCluster];
        for(int i=0;i<graph.numberOfCluster;i++){
            NOVPCinPrivateSpace[i] = graph.listCluster.get(i).listVertex.size();
        }
        int[] cloneChromosome = indiv.Chromosome.clone();
        int[] decodeCloneChromosome = decodeChromosome(graph.totalVertices,cloneChromosome, indiv.NOVPCinCommonSpace, NOVPCinPrivateSpace);
        decodeCloneChromosome = Shaking(decodeCloneChromosome);
        double curLength = calCost(graph,decodeCloneChromosome,NOVPCinPrivateSpace,2);
        //--------------swap-------------
        if(type == 1){
            double minDelta = 0;
            int min_i = -1;
            for(int i=0;i< decodeCloneChromosome.length;i++){
                double deltaLength=0;
                int[] path = swapPath(decodeCloneChromosome.clone(),i);
                deltaLength = calCost(graph,path,NOVPCinPrivateSpace,2) - curLength;
                if(deltaLength < minDelta){
                    minDelta = deltaLength;
                    min_i = i;
                }
            } //TODO: check
            if(minDelta < 0){
                curLength += minDelta;
                decodeCloneChromosome = swapPath(decodeCloneChromosome,min_i);
            }
        }
            //-------------swap--------------
            //------------------2-opt---------------
        else if(type == 2){
            for(int i=0; i < decodeCloneChromosome.length - 1; i++) {
                for(int j=i+1; j < decodeCloneChromosome.length; j++) {
                    double lengthDelta = 0;
                    int[] path = do_2_Opt(decodeCloneChromosome.clone(),i,j);
                    lengthDelta = calCost(graph,path,indiv.NOVPCinCommonSpace,2) - curLength;
                    if (lengthDelta < 0) {
                        decodeCloneChromosome = path;
                        curLength += lengthDelta;
                    }
                }
            }
        }
        //--------------------2-opt----------------------
        cloneChromosome = encodeChromosome(decodeCloneChromosome,cloneChromosome,NOVPCinCommonSpace,NOVPCinPrivateSpace);
        if(curLength < indiv.cost[indiv.skillfactor]){
            Arrays.fill(indiv.cost,Double.MAX_VALUE);
            indiv.cost[indiv.skillfactor] = curLength;
            indiv.Chromosome = cloneChromosome;
            positive = true;
        }
        return positive;
    }
    /**
     * Biến đổi gen sử dụng 2-opt
     *
     * @param  path Gen ban đầu
     * @param i,j 2 điểm để tráo đổi gen
     * @return Gen sau khi biến đổi
     */
    public static int[] do_2_Opt(int[] path, int i, int j){
        int[] x = new int[path.length];
        int countId = 0;
        for (int id=0;id<=i;id++){
            x[countId++] = path[id];
        }
        for (int id=j;id>=i+1;id--){
            x[countId++] = path[id];
        }
        for(int id=j+1;id<path.length;id++){
            x[countId++] = path[id];
        }
        return x;
    }

    /**
     * Biến đổi gen sử dụng swap
     *
     * @param  path Gen ban đầu
     * @param i điểm để tráo đổi gen
     * @return Gen sau khi biến đổi
     */
    public static int[] swapPath(int[] path, int i){
        int[] tempPath = path.clone();
        int temp;
        if(i == tempPath.length-1){
            temp = tempPath[i];
            tempPath[i] = tempPath[0];
            tempPath[0] = temp;
        }else {
            temp = tempPath[i];
            tempPath[i] = tempPath[i+1];
            tempPath[i+1] = temp;
        }
        return tempPath;
    }
}
