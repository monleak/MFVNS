package util;

import basic.Params;

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
        int[] newChromosome = new int[Params.maxTotalVertices];
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
}
