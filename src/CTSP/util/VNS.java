package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;
import TSP.benchmark.Graph;

import java.util.Arrays;

import static CTSP.util.util.codeChromosome;
import static CTSP.util.util.decodeChromosome;

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
     * @param  indiv Cá thể ban đầu
     * @param  type Loại localSearch lựa chọn (1 - swap, 2 - 2opt)
     * @return boolean Có cải thiện sau khi search hay không (T/F)
     */
    public static boolean localSearch(Individual indiv, int type, Graph graph){
        boolean positive = false;
        // int[] path = indiv.Chromosome.clone();
        // path = Shaking(path);
        // path = decodeChromosome(path,prob.graphs.get(indiv.skillfactor).totalVertices);
        // double curLength = 0;
        // for(int i=0;i<path.length-1;i++){
        //     curLength += prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]];
        // }
        // curLength += prob.graphs.get(indiv.skillfactor).distance[path[path.length-1]][path[0]];

        Individual cloneIndiv = indiv.clone();
        cloneIndiv.Chromosome = Shaking(cloneIndiv.Chromosome);
        cloneIndiv.cost[cloneIndiv.skillfactor] = CTSP.util.utilCTSP.calCost(graph,cloneIndiv.Chromosome,cloneIndiv.ClusterOrder,cloneIndiv.NOVPCinCommonSpace);
//        Params.countEvals++;
        //--------------swap-------------
        if(type == 1){
            double minDelta = 0;
            int min_i = -1;
            for(int i=0;i< path.length;i++){
                double deltaLength=0;
                if(i==0){
                    deltaLength = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[path.length-1]] - prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[i+2]]
                            + prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[path.length-1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+2]];
                }else if(i == path.length-1){
                    deltaLength = - prob.graphs.get(indiv.skillfactor).distance[path[i-1]][path[i]] - prob.graphs.get(indiv.skillfactor).distance[path[0]][path[1]]
                            + prob.graphs.get(indiv.skillfactor).distance[path[0]][path[i-1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[1]];
                }else if(i == path.length-2){
                    deltaLength = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i-1]] - prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[0]]
                            + prob.graphs.get(indiv.skillfactor).distance[path[i-1]][path[i+1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[0]];
                }
                else {
                    deltaLength = -prob.graphs.get(indiv.skillfactor).distance[path[i-1]][path[i]] - prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[i+2]]
                            +prob.graphs.get(indiv.skillfactor).distance[path[i-1]][path[i+1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+2]];
                }
                if(deltaLength < minDelta){
                    minDelta = deltaLength;
                    min_i = i;
                }
            }
            if(minDelta < 0){
                curLength = curLength + minDelta;
                path = swapPath(path,min_i);
            }
        } else
            //-------------swap--------------
            //------------------2-opt---------------
            if(type == 2){
                for(int i=0; i < path.length - 1; i++) {
                    for(int j=i+1; j < path.length; j++) {
                        double lengthDelta;
                        if(j != path.length-1){
                            lengthDelta = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]] - prob.graphs.get(indiv.skillfactor).distance[path[j]][path[j+1]]
                                    + prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[j+1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[j]];
                        }else {
                            lengthDelta = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]] - prob.graphs.get(indiv.skillfactor).distance[path[j]][path[0]]
                                    + prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[0]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[j]];
                        }
//                Params.countEvals ++;

                        if (lengthDelta < 0) {
                            path = do_2_Opt(path, i, j);
                            curLength += lengthDelta;
                        }
                    }
                }
            }
        //------------------------------------------

        if(curLength < indiv.cost[indiv.skillfactor]){
//            Individual newIndiv = new Individual(codeChromosome(path,indiv.Chromosome),curLength,indiv.skillfactor);
//            this.pop.pop.add(newIndiv);
            Arrays.fill(indiv.cost,Double.MAX_VALUE);
            indiv.cost[indiv.skillfactor] = curLength;
            indiv.Chromosome = codeChromosome(path,indiv.Chromosome);
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
