package CTSP.util;

import CTSP.basic.Params;
import CTSP.benchmark.Problem;

public class util {
    public static void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = Params.rand.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static int[] decodeChromosome(int[] Chromosome, int totalVertices){
        int[] decodeChromosome = new int[totalVertices];
        int count=0;
        for (int i=0;i<Chromosome.length;i++){
            if(Chromosome[i] < totalVertices){
                decodeChromosome[count++] = Chromosome[i];
            }
        }
        return decodeChromosome;
    }

    public static int[] codeChromosome(int[] decodeChromosome, int[] previousChromosome){
        int[] newChromosome = new int[previousChromosome.length];
        int countDecode = 0;
        for(int i=0;i<newChromosome.length;i++){
            if(previousChromosome[i] >= decodeChromosome.length){
                newChromosome[i] = previousChromosome[i];
            }else {
                newChromosome[i] = decodeChromosome[countDecode++];
            }
        }
        return newChromosome;
    }
    public static int giveId(int x, int y, Problem prob){
        int p1,p2;
        p1 = Math.min(x,y);
        p2 = Math.max(x,y);
        if(p1==0){
            return p1+p2;
        }
        return giveId(p1-1,prob.graphs.size()-1,prob)+p2-p1+1;
    }
}
