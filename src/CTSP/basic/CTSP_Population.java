package CTSP.basic;

import CTSP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CTSP_Population {
    public Problem prob;
    public ArrayList<Individual> pop;
    public double[] best;

    public CTSP_Population(Problem prob){
        this.prob = prob;
        pop = new ArrayList<>();

        this.best = new double[prob.graphs.size()];
        Arrays.fill(best,Double.MAX_VALUE);
    }

    /**
     * Khởi tạo quẩn thể và tính cost
     */
    public void init(){
        while (pop.size() < Params.POP_SIZE){
            Individual individual = new Individual();
            individual.init();
            pop.add(individual);
        }
        for (int elementTestCase = 0;elementTestCase < prob.testCase.get(testCase).length;elementTestCase++){
            //Tính cost
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                pop.get(idIndiv).calCost(prob,prob.testCase.get(testCase)[elementTestCase]);
            }
        }
    }
    public void update(){
        //reset rank
        for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
            pop.get(idIndiv).rank = -1;
        }
        //Set skillfactor, rank and sort pop
        for (int elementTestCase = 0;elementTestCase < prob.testCase.get(testCase).length;elementTestCase++){
            //xếp rank các cá thể theo từng graph
            sortPop(prob.testCase.get(testCase)[elementTestCase]);

            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                if(pop.get(idIndiv).rank == -1 || pop.get(idIndiv).rank > idIndiv){
                    pop.get(idIndiv).rank = idIndiv;
                    pop.get(idIndiv).skillfactor = prob.testCase.get(testCase)[elementTestCase];
                }
            }
            if(best[prob.testCase.get(testCase)[elementTestCase]] > pop.get(0).cost[prob.testCase.get(testCase)[elementTestCase]]){
                best[prob.testCase.get(testCase)[elementTestCase]] = pop.get(0).cost[prob.testCase.get(testCase)[elementTestCase]];
            }
        }
        sortPopByRank();

        while (pop.size() > Params.POP_SIZE){
            pop.remove(pop.size()-1);
        }
    }
    public void sortPop(int idGraph) {
        //Sắp xếp lại các cá thể trong quần thể
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Double.valueOf(o1.cost[idGraph]).compareTo(o2.cost[idGraph]);
            }
        });
    }
    public void sortPopByRank() {
        //Sắp xếp lại các cá thể trong quần thể
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Integer.compare(o1.rank, o2.rank);
            }
        });
    }
}
