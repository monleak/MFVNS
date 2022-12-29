package TSP.main;

import TSP.basic.Params;
import TSP.benchmark.Problem;
import TSP.core.MFVNS;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        Params.recordsNum = 1000;
        Problem prob = new Problem();

        File dir = new File(Params.linkOutput);
        if (!dir.exists()) {
            dir.mkdir();
        }

        for(int testCase = 0;testCase<prob.testCase.size();testCase++){

            String subFolder = Params.linkOutput+"TESTCASE"+testCase+"//";
            File dirTestCase = new File(subFolder);
            if (!dirTestCase.exists()) {
                dirTestCase.mkdir();
            }

            for (int seed = 0; seed < Params.REPT; seed++){
                ArrayList<String> result = new ArrayList<>();

                System.out.println("=============== Test case " + (testCase+1) +" Seed "+ seed +" =================");
                Params.rand = new Random(seed);
                Params.countEvals = 0;
                Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
                MFVNS solver = new MFVNS(prob,testCase);
                solver.run(result);

                //in ra file
                String fitnessFile = subFolder + (seed + 1) + ".txt";
                DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
                for(int i=0;i<result.size();i++){
                    outFit.writeBytes(result.get(i));
                }
                result.clear();
                outFit.close();
            }
        }
    }
}