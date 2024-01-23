package CTSP.core;

import CTSP.basic.Individual;
import CTSP.basic.Params;
import CTSP.basic.CTSP_Population;
import CTSP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;

import static CTSP.util.MFEA.SBX;
import static CTSP.util.VNS.construction_Solution;
import static CTSP.util.VNS.localSearch;
import static CTSP.util.util.*;
import static CTSP.util.util.giveId;
import static CTSP.util.utilCTSP.calCost;
import static CTSP.util.utilCTSP.calCost_with_Penalize_Edge;

public class MFVNS {
    public ArrayList<CTSP_Population> pops;
    public Problem prob;
    public double rmp[]; //Bộ nhớ lịch sử thành công rmp

    /**
     * Lưu trữ các giá trị thành công từng thế hệ
     * Sử dụng để cập nhật bộ nhớ lịch sử thành công sau mỗi thế hệ
     */
    public ArrayList<Double>[] s_rmp;
    public ArrayList<Double>[] diff_f_inter_x; //Sử dụng để tính độ ảnh hưởng của index i đến việc cập nhật rmp[]

    public MFVNS(Problem prob){
        this.prob = prob;

        pops = new ArrayList<>();
        for (int i = 0; i < prob.numberOfGraph; i++) {
            pops.add(new CTSP_Population(prob.graphs.get(i),i));
        }

        rmp = new double[(prob.graphs.size()+1)*prob.graphs.size()/2];
        Arrays.fill(rmp,0.5);
        s_rmp = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        diff_f_inter_x = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        for (int i=0;i<(prob.graphs.size()+1)*prob.graphs.size()/2;i++){
            s_rmp[i] = new ArrayList<>();
            diff_f_inter_x[i] = new ArrayList<>();
        }

//        update();
    }

    /**
     * Chạy và lưu kết quả vào biến result
     * @param result
     */
    public void run(ArrayList<String> result){
        int count = 0;
        boolean stop = false;
        while (count < Params.maxGeneration /*Params.countEvals < Params.maxEvals*/){
            stop = true;
            //----------local search----------
            ArrayList<Integer> typeLocalSearch = new ArrayList<>();
            for(int i=1;i<=2;i++){  //Hiện tại đang có 2 toán tử local search
                typeLocalSearch.add(i);
            }
            for(int i=0;i<prob.numberOfGraph;i++){ //Mỗi đồ thị tạo 1 cá thể local search mới
                if((int)pops.get(i).best_cost <= prob.graphs.get(i).optimal){
                    continue;
                }
                stop = false;
                ArrayList<Integer> cloneTypeLS = new ArrayList<>(typeLocalSearch);
                Individual individual = LocalSearch_Phase(cloneTypeLS,i);
                this.pops.get(i).addToEliteSet(individual);
            }
            //--------------------------------
            if(stop) break;
            //----------MFEA---------------
//            for(int i = 0; i< pop.pop.size(); i++){
//                int j;
//                do{
//                    j = Params.rand.nextInt(pop.pop.size());
//                }while (j==i);
//
//                if(pop.pop.get(i).skillfactor == pop.pop.get(j).skillfactor){
//                    ArrayList<Individual> child = SBX(pop.pop.get(i),pop.pop.get(j),prob);
//                    pop.pop.addAll(child);
//                }else {
//                    double currentRmp = rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)];
//                    if(Params.rand.nextDouble() < currentRmp){
//                        ArrayList<Individual> child = SBX(pop.pop.get(i),pop.pop.get(j),prob);
//                        for(int o=0;o<child.size();o++){
//                            double delta;
//
//                            if(child.get(o).skillfactor == pop.pop.get(i).skillfactor){
//                                delta = pop.pop.get(i).cost[pop.pop.get(i).skillfactor] - child.get(o).cost[pop.pop.get(i).skillfactor];
//                            }else{
//                                delta = pop.pop.get(j).cost[pop.pop.get(j).skillfactor] - child.get(o).cost[pop.pop.get(j).skillfactor];
//                            }
//
//                            if(delta == 0){
//                                pop.pop.add(child.get(o));
//                            }else if(delta > 0){
//                                s_rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(currentRmp);
//                                diff_f_inter_x[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(delta);
//                                pop.pop.add(child.get(o));
//                            }
//                        }
//                    }
//                }
//
//            }
            //------------------------------------

            update();

            String temp = new String();
            System.out.print(count+" "+ Params.countEvals+": ");
            temp += count+" "+ Params.countEvals+": ";
            for (int i=0;i<prob.graphs.size();i++) {
                if((int)this.pops.get(i).best_cost <= prob.graphs.get(i).optimal){
                    System.out.print("*"+this.pops.get(i).best_cost+" ");
                }else {
                    System.out.print(this.pops.get(i).best_cost+" ");
                }
                temp += this.pops.get(i).best_cost+" ";
            }
            System.out.print("\n");
            temp+="\n";
            result.add(temp);

            count++;
        }
    }

    /**
     * Phase local search
     * @param typeLocalSearch Danh sách type local search
     */
    private Individual LocalSearch_Phase(ArrayList<Integer> typeLocalSearch, int task){
        int choose; //Lựa chọn loại localSearch
        boolean positive = false; //Local seach có hiệu quả hay không ?

        double alpha = Params.alphaArray[Params.rand.nextInt(Params.alphaArray.length)];
        int[] S = construction_Solution(alpha, prob.graphs.get(task));
        Individual individual = new Individual(S,task);
        individual.cost = calCost(prob.graphs.get(task),individual.Chromosome);

        while (!typeLocalSearch.isEmpty() && !positive){
            choose = Params.rand.nextInt(typeLocalSearch.size());
            positive = localSearch(individual,typeLocalSearch.get(choose),prob.graphs.get(task));
            typeLocalSearch.remove(choose);
        }
        return individual;
    }

    /**
     * Update quần thể khi qua thế hệ mới
     */
    public void update(){
        // update RMP
        double maxRmp = 0;
        for (int i = 0; i < rmp.length; i++) {
            double good_mean = 0;
            if (s_rmp[i].size() > 0) {
                double sum = 0;
                for (double d : diff_f_inter_x[i]) {
                    sum += d;
                }

                double val1 = 0, val2 = 0, w;
                for (int k = 0; k < s_rmp[i].size(); k++) {
                    w = diff_f_inter_x[i].get(k) / sum;
                    val1 += w * s_rmp[i].get(k) * s_rmp[i].get(k);
                    val2 += w * s_rmp[i].get(k);
                }
                good_mean = val1 / val2;

                if (good_mean > rmp[i] && good_mean > maxRmp) {
                    maxRmp = good_mean;
                }
            }

            double c1 = s_rmp[i].size() > 0 ? 1.0 : 1.0 - Params.C;
            rmp[i] = c1 * rmp[i] + Params.C * good_mean;
            rmp[i] = Math.max(0.01, Math.min(1, rmp[i]));

            s_rmp[i].clear();
            diff_f_inter_x[i].clear();
        }
    }

    private void Select_Elite(Individual individual){
        //TODO:
    }
}
