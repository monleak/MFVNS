package basic;

import benchmark.Graph;
import benchmark.Problem;
import util.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static util.util.shuffleArray;

public class Individual  implements Comparable<Individual> {
    public int[] Chromosome;
    public double[] cost;
    public int skillfactor;
    public int rank;

    public Individual(){
        Chromosome = new int[Params.maxTotalVertices];
        Arrays.fill(Chromosome,-1);

        rank = -1;

        cost = new double[8];
        Arrays.fill(cost,-1);
        skillfactor = -1;
    }
    public Individual(int[] Chromosome, double cost,int skillfactor){
        this.Chromosome = Chromosome;
        rank = -1;

        this.cost = new double[8];
        Arrays.fill(this.cost,-1);
        this.cost[skillfactor] = cost;
        this.skillfactor = skillfactor;
    }

    public void init(){
        int i = 0;
        while(i<Params.maxTotalVertices){
            Chromosome[i] = i;
            i++;
        }

        shuffleArray(Chromosome);
    }

    public void calCost(Problem prob, int idGraph){
        cost[idGraph] = 0;
        int[] decodeChromosome = util.decodeChromosome(Chromosome,prob.graphs.get(idGraph).totalVertices);

        for(int i=0;i<decodeChromosome.length-1;i++){
            cost[idGraph] += prob.graphs.get(idGraph).distance[decodeChromosome[i]][decodeChromosome[i+1]];
        }
        cost[idGraph] += prob.graphs.get(idGraph).distance[decodeChromosome[decodeChromosome.length-1]][decodeChromosome[0]];
        Params.countEvals++;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.valueOf(this.cost[0]).compareTo(o.cost[0]);
    }
}
