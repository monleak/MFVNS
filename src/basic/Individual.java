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
        cost = 0;
        //TODO: kiểm tra lại hàm Individual.calCost
        int point1=0,point2=1;
        for(int i=0;i<prob.graphs.get(idGraph).totalVertices - 1;i++){
            while (point1 < prob.graphs.get(idGraph).totalVertices - 1
                    || Chromosome[point1] > prob.graphs.get(idGraph).totalVertices - 1){
                point1++;
            }
            while (point2 < prob.graphs.get(idGraph).totalVertices
                    || Chromosome[point2] > prob.graphs.get(idGraph).totalVertices
                    || point2 <= point1){
                point2++;
            }
            this.cost += prob.graphs.get(idGraph).distance[Chromosome[point1]][Chromosome[point2]];
        }
    }
}
