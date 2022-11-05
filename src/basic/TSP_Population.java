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
    public void reSizePop(){
        int maxPopSize = pop.size();
        int maxSizePerTask = maxPopSize / prob.testCase.get(testCase).length;
        ArrayList<Individual> tempPop = new ArrayList<>();
        for (int elementTestCase = 0;elementTestCase < prob.testCase.get(testCase).length;elementTestCase++){
            int count = 0;
            sortPop(prob.testCase.get(testCase)[elementTestCase]);
            while (count<maxSizePerTask){
                for (int i = 0;i<pop.size();i++){
                    if(pop.get(i).skillfactor==prob.testCase.get(testCase)[elementTestCase]){
                        tempPop.add(pop.get(i));
                        pop.remove(i);
                        count++;
                        break;
                    }
                }
            }
        }

        sortPop(prob.testCase.get(testCase)[Params.rand.nextInt(prob.testCase.get(testCase).length)]);
        while(tempPop.size() < maxPopSize){
            tempPop.add(pop.get(0));
        }

        this.pop = tempPop;
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
