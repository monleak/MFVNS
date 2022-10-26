package basic;

import benchmark.Graph;
import benchmark.Problem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static util.util.shuffleArray;

public class Individual {
    public int[] Chromosome;
    public double cost;
    public int skillfactor;
    public int rank;

    public Individual(){
        Chromosome = new int[Params.maxTotalVertices];
        Arrays.fill(Chromosome,-1);

        rank = -1;
        cost = -1;
        skillfactor = -1;
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
        cost = 0;
        int[] decodeChromosome = new int[prob.graphs.get(idGraph).totalVertices];
        int count=0;
        for (int i=0;i<Chromosome.length;i++){
            if(Chromosome[i] < prob.graphs.get(idGraph).totalVertices){
                decodeChromosome[count++] = Chromosome[i];
            }
        }

        for(int i=0;i<decodeChromosome.length-1;i++){
            cost += prob.graphs.get(idGraph).distance[decodeChromosome[i]][decodeChromosome[i+1]];
        }
    }
}
