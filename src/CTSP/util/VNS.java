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
     * @return boolean Có cải thiện sau khi search hay không (T/F)
     */
    public static boolean localSearch(Individual indiv, int type, Graph graph){
        boolean positive = false;
        //Shaking
        int[] NOVPCinPrivateSpace = graph.NOVPCinPrivateSpace.clone();
        //--------------swap-------------
        if(type == 1){
            for(int i=0;i<indiv.Chromosome.length;i++){
                int[] cloneChromosome = indiv.Chromosome.clone();
                cloneChromosome = swapPath(cloneChromosome,i);
                double temp_cost = calCost(graph,cloneChromosome);
                if(temp_cost < indiv.cost[indiv.skillfactor]){
                    indiv.Chromosome = cloneChromosome;
                    indiv.cost[indiv.skillfactor] = temp_cost;
                    positive = true;
                    break;
                }
            }
        }
        //-------------swap--------------
        //------------------2-opt---------------
        else if(type == 2){
            for (int i = 0; i < indiv.Chromosome.length - 2; i++) {
                for (int j = i+2; j < indiv.Chromosome.length; j++) {
                    //check xem 2 cạnh có chung cluster hay không
                    int e1_1, e1_2;
                    int e2_1, e2_2;
                    e1_1 = indiv.Chromosome[i];
                    e1_2 = indiv.Chromosome[i+1];

                    if(j==indiv.Chromosome.length-1){
                        if(i==0){
                            //tránh trường hợp 2 cạnh liền kề
                            continue;
                        }
                        e2_1 = indiv.Chromosome[j];
                        e2_2 = indiv.Chromosome[0];
                    }else {
                        e2_1 = indiv.Chromosome[j];
                        e2_2 = indiv.Chromosome[j+1];
                    }

                    boolean check1 = isSameCluster(graph,e1_1,e1_2);
                    boolean check2 = isSameCluster(graph,e2_1,e2_2);
                    boolean check3 = false;
                    if(check1 && check2){
                        if(isSameCluster(graph,e1_1,e2_1)){
                            //tất cả cạnh đều nằm trong cluster
                            check3 = true;
                        }
                    }else if(!check1 && !check2){
                        //tất cả cạnh đều là cạnh liên cụm
                        check3 = true;
                    }

                    if(check3){
                        int[] cloneChromosome = indiv.Chromosome.clone();
                        cloneChromosome = do_2_Opt(cloneChromosome,i,j);
                        double temp_cost = calCost(graph,cloneChromosome);
                        if(temp_cost < indiv.cost[indiv.skillfactor]){
                            indiv.Chromosome = cloneChromosome;
                            indiv.cost[indiv.skillfactor] = temp_cost;
                            positive = true;
                        }
                    }
                }
            }
        }
        //--------------------2-opt----------------------
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
            graph.cal_distance_with_Penalize_Edge();
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
            var RCL_select_s = RCL.get(Params.rand.nextInt(RCL.size()));
            double min_cost_solution = Double.MAX_VALUE;
            int select_index_insert = 0;
            for(int i = 0;i<S.size();i++){
                var clone_S = new ArrayList<>(S);
                clone_S.add(i,RCL_select_s);
                double temp_cost = calCost_with_Penalize_Edge(graph,clone_S.stream().mapToInt(Integer::intValue).toArray());
                if(min_cost_solution > temp_cost){
                    min_cost_solution = temp_cost;
                    select_index_insert = i;
                }
            }
            S.add(select_index_insert,RCL_select_s);
            C.removeIf(vertex -> vertex.id-1 == RCL_select_s);
        }

        return S.stream().mapToInt(Integer::intValue).toArray();
    }
}
