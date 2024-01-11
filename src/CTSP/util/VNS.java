package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;
import CTSP.benchmark.Graph;
import CTSP.benchmark.Vertex;

import java.util.ArrayList;
import java.util.Arrays;

import static CTSP.util.util.*;
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
    public static boolean localSearch(Individual indiv, int type, Graph graph, int[] NOVPCinCommonSpace, int[] pointCommonSpace){
        //TODO: Viết lại. Cấm swap tạo ra các infeasible
        boolean positive = false;
        //Shaking
        int[] NOVPCinPrivateSpace = graph.NOVPCinPrivateSpace;
        int[] decodeChromosome = decodeChromosome(graph.totalVertices, indiv.Chromosome, pointCommonSpace,NOVPCinPrivateSpace, graph.pointPrivateSpace);
        decodeChromosome = Shaking(decodeChromosome);
        double curLength = calCost(graph,decodeChromosome,2,pointCommonSpace);
        //--------------swap-------------
        if(type == 1){
            double minDelta = 0;
            int min_i = -1;
            int decodeLength = decodeChromosome.length;
            for(int i=0;i< decodeLength;i++){
                double deltaLength;
                if(isExists(graph.pointPrivateSpace, i) || //bỏ điểm đầu cluster
                   isExists(graph.pointPrivateSpace, i+1) || i == decodeLength-1 || //bỏ điểm cuối
                   isExists(graph.pointPrivateSpace, i+2) || i == decodeLength-2 //bỏ sát điểm cuối
                 ){
                    int[] newDecodeChromosome = swapPath(decodeChromosome,i);
                    deltaLength = calCost(graph,newDecodeChromosome,2,pointCommonSpace) - curLength;
                }else {
                    int inCluster = inCluster(graph.pointPrivateSpace, i);
                    int[] ClusterSegment = getClusterSegment(decodeChromosome,graph.pointPrivateSpace[inCluster],NOVPCinPrivateSpace[inCluster]);
                    ClusterSegment = convertOrder(ClusterSegment,0);
                    deltaLength = - graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i-graph.pointPrivateSpace[inCluster]]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i-1-graph.pointPrivateSpace[inCluster]]).id-1]
                            - graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+1-graph.pointPrivateSpace[inCluster]]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+2-graph.pointPrivateSpace[inCluster]]).id-1]
                            + graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i-1-graph.pointPrivateSpace[inCluster]]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+1-graph.pointPrivateSpace[inCluster]]).id-1]
                            + graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i-graph.pointPrivateSpace[inCluster]]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+2-graph.pointPrivateSpace[inCluster]]).id-1];
                }
                if(deltaLength < minDelta){
                    minDelta = deltaLength;
                    min_i = i;
                }
            }
            if(minDelta < 0){
                curLength += minDelta;
                decodeChromosome = swapPath(decodeChromosome,min_i);
            }
        }
            //-------------swap--------------
            //------------------2-opt---------------
        else if(type == 2){
            //local search trong cùng cluster
            for(int inCluster = 0;inCluster < NOVPCinPrivateSpace.length;inCluster++){
                int[] ClusterSegment = getClusterSegment(decodeChromosome,graph.pointPrivateSpace[inCluster],NOVPCinPrivateSpace[inCluster]);
                ClusterSegment = convertOrder(ClusterSegment,0);
                for(int i=0;i<NOVPCinPrivateSpace[inCluster]-3;i++){
                    for(int j=i+2;j<NOVPCinPrivateSpace[inCluster]-1;j++){
                        double lengthDelta = - graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+1]).id-1]
                                         - graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[j]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[j+1]).id-1]
                                         + graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i+1]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[j+1]).id-1]
                                         + graph.distance[graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[i]).id-1][graph.listCluster.get(inCluster).listVertex.get(ClusterSegment[j]).id-1];
                        if(lengthDelta < 0){
                            ClusterSegment = do_2_Opt(ClusterSegment,i,j);
                            decodeChromosome = do_2_Opt(decodeChromosome,i+graph.pointPrivateSpace[inCluster],j+graph.pointPrivateSpace[inCluster]);
                            curLength += lengthDelta;
                        }
                    }
                }
            }
            //local search cả các cluster khác
            for(int i=0;i<graph.pointPrivateSpace[graph.numberOfCluster-1];i++){
                //cluster cuối cùng chỉ có thể swap trong chính nó => bỏ qua
                int inCluster = inCluster(graph.pointPrivateSpace, i);
                for(int j=graph.pointPrivateSpace[inCluster+1];j<decodeChromosome.length;j++){
                    int[] newDecodeChromosome = do_2_Opt(decodeChromosome,i,j);
                    double newCost = calCost(graph,newDecodeChromosome,2,pointCommonSpace);
                    if(newCost < curLength){
                        curLength = newCost;
                        decodeChromosome = newDecodeChromosome;
                    }
                }
            }
        }
        //--------------------2-opt----------------------
        if(curLength < indiv.cost[indiv.skillfactor]){
            Arrays.fill(indiv.cost,Double.MAX_VALUE);
            indiv.cost[indiv.skillfactor] = curLength;
            indiv.Chromosome = encodeChromosome(decodeChromosome, indiv.Chromosome, NOVPCinCommonSpace,NOVPCinPrivateSpace,graph.pointPrivateSpace);
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

    public static int[] construction_Solution(double alpha, Graph graph){
        if(graph.distance_with_Penalize_Edge == null){
            graph.distance_with_Penalize_Edge = graph.distance.clone();

            //Bắt đầu: Phạt cạnh liên cụm
            double max_dis = 0;
            for(int i=0;i<graph.distance_with_Penalize_Edge.length;i++){
                for(int j=0;j<graph.distance_with_Penalize_Edge[i].length;j++){
                    if(max_dis < graph.distance_with_Penalize_Edge[i][j]){
                        max_dis = graph.distance_with_Penalize_Edge[i][j];
                    }
                }
            }
            double Penalize_Edge = 10*max_dis;
            for(int i=0;i<graph.distance_with_Penalize_Edge.length;i++){
                for(int j=0;j<graph.distance_with_Penalize_Edge[i].length;j++){
                    if(i != j){
                        graph.distance_with_Penalize_Edge[i][j] += Penalize_Edge;
                    }
                }
            }
            for(var cluster : graph.listCluster){
                for(int i = 0;i<cluster.listIDVertex.size();i++){
                    int v1 = cluster.listIDVertex.get(i) - 1;
                    for(int j=i+1;j<cluster.listIDVertex.size();j++){
                        int v2 = cluster.listIDVertex.get(j) - 1;
                        graph.distance_with_Penalize_Edge[v1][v2] -= Penalize_Edge;
                        graph.distance_with_Penalize_Edge[v2][v1] -= Penalize_Edge;
                    }
                }
            }
            //Kết thúc: Phạt cạnh liên cụm
        }

        //Bắt đầu: Chọn 3 đỉnh
        int select_v = Params.rand.nextInt(graph.totalVertices);
        int select_v1=select_v+1,select_v2=select_v+2;
        double dis_v1 = Double.MAX_VALUE,dis_v2 = Double.MAX_VALUE;
        for(int i = 0;i<graph.distance_with_Penalize_Edge[select_v].length;i++){
            if(i == select_v){
                continue;
            }
            if(dis_v1 > graph.distance_with_Penalize_Edge[select_v][i]){
                dis_v1 = graph.distance_with_Penalize_Edge[select_v][i];
                select_v1 = i;
            }
            if(dis_v2 > graph.distance_with_Penalize_Edge[select_v][i] && dis_v1 < graph.distance_with_Penalize_Edge[select_v][i]){
                dis_v2 = graph.distance_with_Penalize_Edge[select_v][i];
                select_v2 = i;
            }
        }
        //Két thúc: Chọn 3 đỉnh

        ArrayList<Vertex> C = new ArrayList<>(graph.vertexList);
        ArrayList<Integer> S = new ArrayList<>();
        S.add(select_v);S.add(select_v1);S.add(select_v2);
        C.removeIf(vertex -> S.contains(vertex.id-1));

        while (!C.isEmpty()){
            //Bắt đầu: Tìm k đỉnh gần với S
            ArrayList<Integer> NV = new ArrayList<>();
            ArrayList<Double> IC = new ArrayList<>();
            for(int i=0;i<S.size();i++){
                if((S.size() + NV.size()) == graph.totalVertices){
                    break;
                }
                int temp_select_id = 0;
                double temp_dis = Double.MAX_VALUE;
                for(int j=0;j<graph.distance_with_Penalize_Edge[S.get(i)].length;j++){
                    if(!S.contains(j) &&
                            !NV.contains(j) &&
                            temp_dis > graph.distance_with_Penalize_Edge[S.get(i)][j]){
                        temp_dis = graph.distance_with_Penalize_Edge[S.get(i)][j];
                        temp_select_id = j;
                    }
                }
                NV.add(temp_select_id);
                IC.add(temp_dis);
            }
            double min_ic = IC.stream().mapToDouble(Double::doubleValue).min().orElseThrow(NullPointerException::new);
            double max_ic = IC.stream().mapToDouble(Double::doubleValue).max().orElseThrow(NullPointerException::new);
            double condition_ic = min_ic + alpha * (max_ic - min_ic);
            //Kết thúc: Tìm k đỉnh gần với S

            //Bắt đầu: Lựa chọn RCL
            ArrayList<Integer> RCL = new ArrayList<>();
            for(int i=0;i<IC.size();i++){
                if(IC.get(i) <= condition_ic){
                    RCL.add(NV.get(i));
                }
            }
            //Kết thúc: Lựa chọn RCL
            int temp_select_s = RCL.get(Params.rand.nextInt(RCL.size()));
            S.add(temp_select_s);
            C.removeIf(vertex -> vertex.id-1 == temp_select_s);
        }

        return S.stream().mapToInt(Integer::intValue).toArray();
    }
}
