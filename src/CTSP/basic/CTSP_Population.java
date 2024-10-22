package CTSP.basic;

import CTSP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static CTSP.util.utilCTSP.calCost;

public class CTSP_Population {
    public Problem prob;
    public ArrayList<Individual> pop;
    public double[] best;

    public CTSP_Population(Problem prob){
        this.prob = prob;
        pop = new ArrayList<>();
        while (pop.size() < Params.POP_SIZE){
            Individual individual = new Individual(prob.maxTotalVertices, prob.numberOfGraph);
            for(int i=0;i<prob.numberOfGraph;i++){
                individual.cost[i] = calCost(prob.graphs.get(i),individual.Chromosome,1, prob.pointCommonSpace);
            }
            pop.add(individual);
        }

        this.best = new double[prob.graphs.size()];
        Arrays.fill(best,Double.MAX_VALUE);

        this.update();
    }

    /**
     * Cập nhật skillfactor và sizePop
     * Cập nhật best
     * Cập nhật kích thước quần thể
     */
    public void update(){
        //reset rank
        for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
            pop.get(idIndiv).rank = -1;
        }
        //Set skillfactor, rank and sort pop
        for(int task=0;task<prob.numberOfGraph;task++){
            sortPop(task);
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                if(pop.get(idIndiv).rank == -1 || pop.get(idIndiv).rank > idIndiv){
                    pop.get(idIndiv).rank = idIndiv;
                    pop.get(idIndiv).skillfactor = task;
                }
            }
            if(best[task] > pop.get(0).cost[task]){
                best[task] = pop.get(0).cost[task];
            }
        }
        sortPopByRank();

        while (pop.size() > Params.POP_SIZE){
            pop.remove(pop.size()-1);
        }
    }

    /**
     * Sắp xếp lại các cá thể trong quần thể dựa trên cost
     * @param idGraph
     */
    public void sortPop(int idGraph) {
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Double.valueOf(o1.cost[idGraph]).compareTo(o2.cost[idGraph]);
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
