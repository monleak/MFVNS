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

    public void update(double[] best){
        for (int skillfactor = 0;skillfactor < prob.testCase.get(testCase).length;skillfactor++){
            //Tính cost và xếp rank các cá thể theo từng graph
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                pop.get(idIndiv).calCost(prob,prob.testCase.get(testCase)[skillfactor]);
            }
            sortPop(skillfactor);
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                if(pop.get(idIndiv).rank == -1 || pop.get(idIndiv).rank > idIndiv){
                    pop.get(idIndiv).rank = idIndiv;
                    pop.get(idIndiv).skillfactor = skillfactor;
                }
            }
            best[skillfactor] = pop.get(0).cost[skillfactor];
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
