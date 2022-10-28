package core;

import basic.Individual;
import basic.Params;
import basic.TSP_Population;
import benchmark.Problem;

import java.awt.*;
import java.util.Arrays;

import static util.util.codeChromosome;
import static util.util.decodeChromosome;

public class MFVNS {
    //TODO: Xử lý tác vụ
    public TSP_Population pop;
    public double[] best;
    public Problem prob;
    public int testCase;
    public MFVNS(Problem prob, int testCase){
        this.prob = prob;
        this.testCase = testCase;
        best = new double[8];
        Arrays.fill(best,Double.MAX_VALUE);

        pop = new TSP_Population(prob,testCase);
        pop.init();
        pop.update(best);
    }
    public void run(){
        int count = 0;
        while (Params.countEvals < Params.maxEvals){
            for(int i=0;i<pop.pop.size();i++){
                for (int k=0;k<Params.kmax;k++){
                    if(localSearch(pop.pop.get(i),k)){
                        break;
                    }
                }

            }
            pop.update(best);

            System.out.print(count+": ");
            for (double a: best) {
                System.out.print(a+" ");
            }
            System.out.print("\n");
            count++;
        }
    }
    public int[] Shaking(int[] Chromosome, int k){
        int[] x = Chromosome.clone();
        for (int i=0;i<k;i++){
            int p1,p2;
            p1 = Params.rand.nextInt(Chromosome.length);
            do{
                p2 = Params.rand.nextInt(Chromosome.length);
            }while (p2==p1);

            int temp = x[p1];
            x[p1] = x[p2];
            x[p2] = temp;
        }
        return x;
    }
    public boolean localSearch(Individual indiv, int k){

        int[] path = decodeChromosome(Shaking(indiv.Chromosome,k),prob.graphs.get(indiv.skillfactor).totalVertices);

        int i = Params.rand.nextInt(path.length-2);
        int j = Math.min(i+5,path.length-1);

        path = do_2_Opt(path, i, j);
        double pathLength = 0;
        for(int x=0;x<path.length-1;x++){
            pathLength += prob.graphs.get(indiv.skillfactor).distance[path[x]][path[x+1]];
        }
        pathLength += prob.graphs.get(indiv.skillfactor).distance[path[path.length-1]][path[0]];
        Params.countEvals++;

        double lengthDelta = pathLength - indiv.cost[indiv.skillfactor];
        if (lengthDelta < 0) {
            indiv.Chromosome = codeChromosome(path,indiv.Chromosome);
            indiv.cost[indiv.skillfactor] = pathLength;
            return true;
        }
        return false;
    }
    public int[] do_2_Opt(int[] path, int i, int j){
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
}
