package basic;

import benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TSP_Population {
    public Problem prob;
    public int testCase;
    public ArrayList<Individual> pop;

    public TSP_Population(Problem prob, int testCase){
        this.prob = prob;
        pop = new ArrayList<>();
        this.testCase = testCase;
    }

    public void init(){
        while (pop.size() < Params.POP_SIZE){
            Individual individual = new Individual();
            individual.init();
            pop.add(individual);
        }
    }

    public void setting(double[] best){
        //Set skillfactor
        for (int elementTestCase = 0;elementTestCase < prob.testCase.get(testCase).length;elementTestCase++){
            //Tính cost và xếp rank các cá thể theo từng graph
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                pop.get(idIndiv).calCost(prob,prob.testCase.get(testCase)[elementTestCase]);
            }
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
    }
    public void reSizePop(double[] best){
        setting(best);
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
