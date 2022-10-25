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
        int i = 0;
        while(i<Params.maxTotalVertices){
            Chromosome[i] = i;
            i++;
        }

        rank = -1;
        cost = -1;
        skillfactor = -1;
    }

    public void init(){
        shuffleArray(Chromosome);
    }

    public void calCost(Problem prob, int idGraph){
        skillfactor = idGraph;
        int point1=0,point2=0;
        for(int i=0;i<prob.graphs.get(idGraph).totalVertices - 1;i++){

        }
    }
}
