package CTSP.basic;

import CTSP.benchmark.Graph;
import CTSP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static CTSP.util.utilCTSP.calCost;
import static CTSP.util.utilCTSP.calDif;

public class CTSP_Population {
    public Graph graph;
    public ArrayList<Individual> pop;
    public double best_cost;
    public int id_task; // Bắt đầu đếm từ 0 giống skill factor của indiv

    public CTSP_Population(Graph graph, int id_task){
        this.graph = graph;
        pop = new ArrayList<>();
        this.id_task = id_task;
        this.best_cost = Double.MAX_VALUE;
    }

    /**
     * Cập nhật skillfactor và sizePop
     * Cập nhật best
     * Cập nhật kích thước quần thể
     */
    public void update(){
        //reset rank
//        for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
//            pop.get(idIndiv).rank = -1;
//        }
//        //Set skillfactor, rank and sort pop
//        for(int task=0;task<prob.numberOfGraph;task++){
//            sortPop(task);
//            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
//                if(pop.get(idIndiv).rank == -1 || pop.get(idIndiv).rank > idIndiv){
//                    pop.get(idIndiv).rank = idIndiv;
//                    pop.get(idIndiv).skillfactor = task;
//                }
//            }
//            if(best[task] > pop.get(0).cost[task]){
//                best[task] = pop.get(0).cost[task];
//            }
//        }
//        sortPopByRank();
//
//        while (pop.size() > Params.POP_SIZE){
//            pop.remove(pop.size()-1);
//        }
    }

    /**
     * Thêm 1 cá thể vào quần thể có sẵn
     * @param indiv
     */
    public void addToEliteSet(Individual indiv){
        if(indiv.cost < best_cost){
            best_cost = indiv.cost;
        }
        if(pop.size() < Params.POP_SIZE){
            pop.add(indiv);
        }else {
            sortPop();
            if(indiv.cost < this.pop.get(this.pop.size()-1).cost){
                int count = 0;
                for (int i = 0; i < this.pop.size(); i++) {
                    if(calDif(indiv,this.pop.get(i)) > Params.mindif){
                        count++;
                    }
                }
                if(count == this.pop.size()){
                    int value_dif = calDif(indiv,this.pop.get(this.pop.size()-1));
                    int index_out = this.pop.size()-1;
                    for (int i = 0; i < this.pop.size(); i++) {
                        int value_dif2 = calDif(indiv,this.pop.get(i));
                        if((indiv.cost < this.pop.get(i).cost) && (value_dif2 < value_dif)){
                            value_dif = value_dif2;
                            index_out = i;
                        }
                    }
                    this.pop.remove(index_out);
                    this.pop.add(indiv);
                }
            }
        }
    }

    /**
     * Sắp xếp lại các cá thể trong quần thể dựa trên cost
     */
    public void sortPop() {
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Double.valueOf(o1.cost).compareTo(o2.cost);
            }
        });
    }

    /**
     * Sắp xếp lại các cá thể trong quần thể dựa trên rank
     */
    public void sortPopByRank() {
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Integer.compare(o1.rank, o2.rank);
            }
        });
    }
}
