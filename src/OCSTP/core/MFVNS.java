package OCSTP.core;

import OCSTP.basic.Individual;
import OCSTP.basic.Params;
import OCSTP.basic.OCSTP_Population;
import OCSTP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;

import static OCSTP.util.MFEA.Crossover;
import static OCSTP.util.VNS.localSearch;
import static OCSTP.util.util.*;
import static OCSTP.util.util.giveId;

public class MFVNS {
    public OCSTP_Population pop;
    public double[] best;
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
        best = new double[prob.graphs.size()];
        Arrays.fill(best,Double.MAX_VALUE);

        pop = new OCSTP_Population(prob);

        rmp = new double[(prob.graphs.size()+1)*prob.graphs.size()/2];
        Arrays.fill(rmp,0.5);
        s_rmp = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        diff_f_inter_x = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        for (int i=0;i<(prob.graphs.size()+1)*prob.graphs.size()/2;i++){
            s_rmp[i] = new ArrayList<>();
            diff_f_inter_x[i] = new ArrayList<>();
        }
        update();
    }

    /**
     * Chạy và lưu kết quả vào biến result
     * @param result
     */
    public void run(ArrayList<String> result){
        int count = 0;
        boolean stop = false;
        long startTime = System.currentTimeMillis();
        while (count < Params.maxGeneration /*Params.countEvals < Params.maxEvals*/){
            stop = true;
            boolean checkIndivPositive = false;
            for(int i=0;i<pop.pop.size();i++){
                if((int)best[pop.pop.get(i).skillfactor] <= prob.graphs.get(pop.pop.get(i).skillfactor).optimal){
                    continue;
                }
                stop = false;

                if(pop.pop.get(i).countNegative >= Params.MAX_NEGATIVE){
                    continue;
                }
                checkIndivPositive = true;
                boolean positive; //Local seach có hiệu quả hay không ?
                positive = localSearch(pop.pop.get(i),prob.graphs.get(pop.pop.get(i).skillfactor),prob.maxTotalVertices);
                if(!positive){
                    pop.pop.get(i).countNegative++;
                }else{
                    pop.pop.get(i).countNegative = 0;
                }
            }

            //--------------------------------
            if(stop || !checkIndivPositive) break;

            //----------MFEA---------------
            for(int i = 0; i< Params.POP_SIZE; i++){
                int j;
                do{
                    j = Params.rand.nextInt(pop.pop.size());
                }while (j==i);

                if(pop.pop.get(i).skillfactor == pop.pop.get(j).skillfactor){
                    ArrayList<Individual> child = Crossover(pop.pop.get(i),pop.pop.get(j),prob);
                    pop.pop.addAll(child);
                }else {
                    double currentRmp = rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)];
                    if(Params.rand.nextDouble() < currentRmp){
                        ArrayList<Individual> child = Crossover(pop.pop.get(i),pop.pop.get(j),prob);
                        for(int o=0;o<child.size();o++){
                            double delta;

                            if(child.get(o).skillfactor == pop.pop.get(i).skillfactor){
                                delta = pop.pop.get(i).cost[pop.pop.get(i).skillfactor] - child.get(o).cost[pop.pop.get(i).skillfactor];
                            }else{
                                delta = pop.pop.get(j).cost[pop.pop.get(j).skillfactor] - child.get(o).cost[pop.pop.get(j).skillfactor];
                            }

                            if(delta == 0){
                                pop.pop.add(child.get(o));
                            }else if(delta > 0){
                                s_rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(currentRmp);
                                diff_f_inter_x[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(delta);
                                pop.pop.add(child.get(o));
                            }
                        }
                    }
                }

            }
            //------------------------------------

            update();

            String temp = new String();
            System.out.print(count+" "+ Params.countEvals+": ");
            temp += count+" "+ Params.countEvals+": ";
            for (int i=0;i<prob.graphs.size();i++) {
                if((int)best[i] <= prob.graphs.get(i).optimal){
                    System.out.print("*"+best[i]+" ");
                }else {
                    System.out.print(best[i]+" ");
                }
                temp += best[i]+" ";
            }
            System.out.print("\n");
            temp+="\n";
            result.add(temp);

//            System.out.print(count+": ");
//            for (double a :
//                    rmp) {
//                System.out.print(a + " ");
//            }
//            System.out.print("\n");

            count++;
        }
        long endTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (endTime - startTime)/1000.0;
        System.out.println("Thoi gian chay: "+ elapsedTimeInSeconds+" giay");
        result.add("Thoi gian chay: "+ elapsedTimeInSeconds+" giay\n");
    }

    /**
     * Update quần thể khi qua thế hệ mới
     */
    public void update(){
        pop.update();
        this.best = pop.best.clone();

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


}