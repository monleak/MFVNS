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

    public void update(){
        for (int skillfactor = 0;skillfactor < prob.testCase.get(testCase).length;skillfactor++){
            //Tính cost và xếp rank các cá thể theo từng graph
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                pop.get(idIndiv).calCost(prob,prob.testCase.get(testCase)[skillfactor]);
            }
            sortPop();
            for(int idIndiv = 0;idIndiv < pop.size();idIndiv++){
                if(pop.get(idIndiv).rank == -1 || pop.get(idIndiv).rank > idIndiv){
                    pop.get(idIndiv).rank = idIndiv;
                    pop.get(idIndiv).skillfactor = skillfactor;
                }
            }
        }
        sortPopByRank();

        while (pop.size() > Params.POP_SIZE){
            pop.remove(pop.size()-1);
        }
    }
    public void sortPop() {
        //Sắp xếp lại các cá thể trong quần thể
        this.pop.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Double.valueOf(o1.cost).compareTo(o2.cost);
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
