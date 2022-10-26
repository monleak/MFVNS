package core;

import basic.Individual;
import basic.Params;
import basic.TSP_Population;
import benchmark.Problem;

import java.awt.*;

public class MFVNS {
    //TODO: Xử lý tác vụ
    public TSP_Population pop;
    public Problem prob;
    public int testCase;
    public MFVNS(Problem prob, int testCase){
        this.prob = prob;
        this.testCase = testCase;

        pop = new TSP_Population(prob,testCase);
        pop.init();
        pop.update();
    }
    public void run(){

    }
    public int[] Shaking(int[] Chromosome, int k){
        int[] x = Chromosome.clone();
        for (int i=0;i<k;i++){
            int p1,p2;
            p1 = Params.rand.nextInt(Params.maxTotalVertices);
            do{
                p2 = Params.rand.nextInt(Params.maxTotalVertices);
            }while (p2==p1);

            int temp = x[p1];
            x[p1] = x[p2];
            x[p2] = temp;
        }
        return x;
    }
    public int[] localSearch(int[] ChromosomeAfterShaking, int idGraph){
        int[] path = ChromosomeAfterShaking.clone();

        double curLength = 0;
        for(int i=0;i<path.length-1;i++){
            curLength += prob.graphs.get(idGraph).distance[path[i]][path[i+1]];
        }
        curLength += prob.graphs.get(idGraph).distance[path[path.length-1]][path[0]];

        boolean foundImprovement = true;
        while (foundImprovement){
            foundImprovement = false;
            for(int i=0; i <= path.length - 2; i++) {
                for(int j=i+1; j <= path.length - 1; j++) {
                    double lengthDelta = - prob.graphs.get(idGraph).distance[path[i]][path[i+1]] - prob.graphs.get(idGraph).distance[path[j]][path[j+1]]
                            + prob.graphs.get(idGraph).distance[path[i+1]][path[j+1]] + prob.graphs.get(idGraph).distance[path[i]][path[j]];

                    if (lengthDelta < 0) {
                        path = do_2_Opt(path, i, j);
                        curLength += lengthDelta;
                        foundImprovement = true;
                    }
                }
            }
        }
        return path;
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
