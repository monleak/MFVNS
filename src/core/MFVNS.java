package core;

import basic.Individual;
import basic.Params;
import basic.TSP_Population;
import benchmark.Problem;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static util.util.*;

public class MFVNS {
    //TODO: Xử lý tác vụ
    public TSP_Population pop;
    public double[] best;
    public Problem prob;
    public int testCase;

    public double rmp[]; //Bộ nhớ lịch sử thành công rmp
    public ArrayList<Double>[] s_rmp; //   Lưu trữ các giá trị thành công từng thế hệ
                                    //     Sử dụng để cập nhật bộ nhớ lịch sử thành công sau mỗi thế hệ
    public ArrayList<Double>[] diff_f_inter_x; //Sử dụng để tính độ ảnh hưởng của index i đến việc cập nhật rmp[]

    public MFVNS(Problem prob, int testCase){
        this.prob = prob;
        this.testCase = testCase;
        best = new double[prob.graphs.size()];
        Arrays.fill(best,Double.MAX_VALUE);

        pop = new TSP_Population(prob,testCase);
        pop.init();

        rmp = new double[(prob.graphs.size()+1)*prob.graphs.size()/2];
        Arrays.fill(rmp,0.5);
        s_rmp = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        diff_f_inter_x = new ArrayList[(prob.graphs.size()+1)*prob.graphs.size()/2];
        for (int i=0;i<(prob.graphs.size()+1)*prob.graphs.size()/2;i++){
            s_rmp[i] = new ArrayList<>();
            diff_f_inter_x[i] = new ArrayList<>();
        }

        update();
    }
    public void run(ArrayList<String> result){
        int count = 0;
        while (/*count < Params.maxGeneration*/ Params.countEvals < Params.maxEvals){
            for(int i=0;i<pop.pop.size();i++){
                localSearch(pop.pop.get(i));
            }

            for(int i=0;i<Params.POP_SIZE;i++){
                int j;
                do{
                    j = Params.rand.nextInt(pop.pop.size());
                }while (j==i);

                if(pop.pop.get(i).skillfactor == pop.pop.get(j).skillfactor){
                    ArrayList<Individual> child = SBX(pop.pop.get(i),pop.pop.get(j));
                    pop.pop.addAll(child);
                }else {
                    double currentRmp = rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)];
                    if(Params.rand.nextDouble() < currentRmp){
                        ArrayList<Individual> child = SBX(pop.pop.get(i),pop.pop.get(j));
                        for(int o=0;o<child.size();o++){
                            double delta;

                            if(child.get(o).skillfactor == pop.pop.get(i).skillfactor){
                                delta = pop.pop.get(i).cost[pop.pop.get(i).skillfactor] - child.get(o).cost[pop.pop.get(i).skillfactor];
                            }else{
                                delta = pop.pop.get(j).cost[pop.pop.get(j).skillfactor] - child.get(o).cost[pop.pop.get(j).skillfactor];
                            }

                            if(delta == 0){
                                pop.pop.add(child.get(o));
                            }else if(delta > 0){
                                s_rmp[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(currentRmp);
                                diff_f_inter_x[giveId(pop.pop.get(i).skillfactor,pop.pop.get(j).skillfactor,prob)].add(delta);
                                pop.pop.add(child.get(o));
                            }
                        }
                    }
                }

            }
            update();

            String temp = new String();
            System.out.print(count+" "+Params.countEvals+": ");
            temp += count+" "+Params.countEvals+": ";
            for (int i=0;i<prob.testCase.get(testCase).length;i++) {
                System.out.print(best[prob.testCase.get(testCase)[i]]+" ");
                temp +=best[prob.testCase.get(testCase)[i]]+" ";
            }
            System.out.print("\n");
            temp+="\n";
            result.add(temp);

//            System.out.print(count+": ");
//            for (double a :
//                    rmp) {
//                System.out.print(a + " ");
//            }
//            System.out.print("\n");

            count++;
        }
    }
    public void update(){
        pop.update();
        this.best = pop.best.clone();

        // update RMP
        double maxRmp = 0;
        for (int i = 0; i < rmp.length; i++) {
            double good_mean = 0;
            if (s_rmp[i].size() > 0) {
                double sum = 0;
                for (double d : diff_f_inter_x[i]) {
                    sum += d;
                }

                double val1 = 0, val2 = 0, w;
                for (int k = 0; k < s_rmp[i].size(); k++) {
                    w = diff_f_inter_x[i].get(k) / sum;
                    val1 += w * s_rmp[i].get(k) * s_rmp[i].get(k);
                    val2 += w * s_rmp[i].get(k);
                }
                good_mean = val1 / val2;

                if (good_mean > rmp[i] && good_mean > maxRmp) {
                    maxRmp = good_mean;
                }
            }

            double c1 = s_rmp[i].size() > 0 ? 1.0 : 1.0 - Params.C;
            rmp[i] = c1 * rmp[i] + Params.C * good_mean;
            rmp[i] = Math.max(0.01, Math.min(1, rmp[i]));

            s_rmp[i].clear();
            diff_f_inter_x[i].clear();
        }
    }
    public ArrayList<Individual> SBX(Individual parrentA, Individual parrentB){
        Individual o1 = new Individual();o1.init();
        Individual o2 = new Individual();o2.init();

        int[] ChromosomeA = parrentA.Chromosome.clone();
        int[] ChromosomeB = parrentB.Chromosome.clone();

        int point1,point2;
        point1 = Params.rand.nextInt(Params.maxTotalVertices);
        do{
            point2 = Params.rand.nextInt(Params.maxTotalVertices);
        }while (point2 == point1);

        if(point1 > point2){
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for(int i=point1;i<=point2;i++){
            o1.Chromosome[i]=parrentA.Chromosome[i];
            for (int j=0;j<Params.maxTotalVertices;j++){
                if(ChromosomeB[j]==parrentA.Chromosome[i]){
                    ChromosomeB[j] = -1;
                    break;
                }
            }
            o2.Chromosome[i]=parrentB.Chromosome[i];
            for (int j=0;j<Params.maxTotalVertices;j++){
                if(ChromosomeA[j]==parrentB.Chromosome[i]){
                    ChromosomeA[j] = -1;
                    break;
                }
            }
        }
        int count1=0,count2=0;
        for (int i=0;i<Params.maxTotalVertices;i++){
            if(count1 == point1)
                count1 = point2+1;
            if(count2 == point1)
                count2 = point2+1;
            if(ChromosomeA[i] != -1){
                o2.Chromosome[count1++] = ChromosomeA[i];
            }
            if(ChromosomeB[i] != -1){
                o1.Chromosome[count2++] = ChromosomeB[i];
            }
        }

        o1.skillfactor = Params.rand.nextBoolean() ? parrentA.skillfactor : parrentB.skillfactor;
        o2.skillfactor = Params.rand.nextBoolean() ? parrentA.skillfactor : parrentB.skillfactor;

        o1.calCost(prob,o1.skillfactor);
        o2.calCost(prob,o2.skillfactor);

        ArrayList<Individual> child = new ArrayList<>();
        child.add(o1);child.add(o2);

        return child;
    }
    public static int[] Shaking(int[] Chromosome){
        int[] x = Chromosome.clone();
        if(Chromosome.length < 8){
            int p1,p2;
            p1 = Params.rand.nextInt(Params.maxTotalVertices);
            do{
                p2 = Params.rand.nextInt(Params.maxTotalVertices);
            }while (p2==p1);

            int temp = x[p1];
            x[p1] = x[p2];
            x[p2] = temp;
        } else {
            //Using double bridge
            int[] allowSelect = new int[x.length];
            for (int i=0;i<x.length;i++){
                allowSelect[i] = i;
            }
            int[] point = new int[4];
            for (int i=0;i<point.length;i++){
                int select;
                do{
                    select = Params.rand.nextInt(allowSelect.length);
                }while (allowSelect[select] == -1);

                point[i] = allowSelect[select];
                allowSelect[select] = -1;
                //select + 1
                if(select == x.length-1)
                    allowSelect[0] = -1;
                else
                    allowSelect[select+1] = -1;

                //select - 1
                if(select == 0)
                    allowSelect[x.length - 1] =-1;
                else
                    allowSelect[select - 1] = -1;
            }
            Arrays.sort(point);

            int[] tempX = new int[x.length];
            int count = 0;
            tempX[count++] = x[point[0]];
            for (int i = point[2]+1;i<=point[3];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[1]+1;i<=point[2];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[0]+1;i<=point[1];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for(int i=point[3]+1;i<x.length;i++){
                tempX[count++] = x[i];
            }
            for(int i=0;i<point[0];i++){
                tempX[count++] = x[i];
            }

            x = tempX;
        }
        return x;
    }
    public void localSearch(Individual indiv){
        int[] path = indiv.Chromosome.clone();
        path = Shaking(path);
        path = decodeChromosome(path,prob.graphs.get(indiv.skillfactor).totalVertices);

        double curLength = 0;
        for(int i=0;i<path.length-1;i++){
            curLength += prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]];
        }
        curLength += prob.graphs.get(indiv.skillfactor).distance[path[path.length-1]][path[0]];

        for(int i=0; i < path.length - 1; i++) {
            for(int j=i+1; j < path.length; j++) {
                double lengthDelta;
                if(j != path.length-1){
                    lengthDelta = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]] - prob.graphs.get(indiv.skillfactor).distance[path[j]][path[j+1]]
                            + prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[j+1]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[j]];
                }else {
                    lengthDelta = - prob.graphs.get(indiv.skillfactor).distance[path[i]][path[i+1]] - prob.graphs.get(indiv.skillfactor).distance[path[j]][path[0]]
                            + prob.graphs.get(indiv.skillfactor).distance[path[i+1]][path[0]] + prob.graphs.get(indiv.skillfactor).distance[path[i]][path[j]];
                }
//                Params.countEvals++;

                if (lengthDelta < 0) {
                    path = do_2_Opt(path, i, j);
                    curLength += lengthDelta;
                }
            }
        }
        if(curLength < indiv.cost[indiv.skillfactor]){
//            Individual newIndiv = new Individual(codeChromosome(path,indiv.Chromosome),curLength,indiv.skillfactor);
//            this.pop.pop.add(newIndiv);
            Arrays.fill(indiv.cost,Double.MAX_VALUE);
            indiv.cost[indiv.skillfactor] = curLength;
            indiv.Chromosome = codeChromosome(path,indiv.Chromosome);
        }
    }
    public static int[] do_2_Opt(int[] path, int i, int j){
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
