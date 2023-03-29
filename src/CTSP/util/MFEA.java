package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;

import java.util.ArrayList;

public class MFEA {
    //TODO: Chuyển các hàm thuộc xử lý của MFEA từ core.MFVNS qua bên này

    /**
     * Lai ghép 2 cá thể ban đầu để tạo ra 2 con mới
     *
     * @param  parrentA
     * @param  parrentB
     * @return 2 con mới
     */
    public static ArrayList<Individual> SBX(Individual parrentA, Individual parrentB){
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
            for (int j = 0; j< Params.maxTotalVertices; j++){
                if(ChromosomeB[j]==parrentA.Chromosome[i]){
                    ChromosomeB[j] = -1;
                    break;
                }
            }
            o2.Chromosome[i]=parrentB.Chromosome[i];
            for (int j = 0; j< Params.maxTotalVertices; j++){
                if(ChromosomeA[j]==parrentB.Chromosome[i]){
                    ChromosomeA[j] = -1;
                    break;
                }
            }
        }
        int count1=0,count2=0;
        for (int i = 0; i< Params.maxTotalVertices; i++){
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
}
