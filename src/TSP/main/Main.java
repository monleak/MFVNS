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

        File dir = new File(Params.linkOutput); //Tạo thư mục chứa kết quả nếu chưa có
        if (!dir.exists()) {
            dir.mkdir();
        }

        File subDir = new File(Params.linkOutput+Params.linkOutputTSP); //Tạo thư mục con chứa kết quả TSP nếu chưa có
        if (!subDir.exists()) {
            subDir.mkdir();
        }
        for(int testCase = 0;testCase<prob.testCase.size();testCase++){

            String subFolder = Params.linkOutput+Params.linkOutputTSP+"TESTCASE"+testCase+"//";
            File dirTestCase = new File(subFolder);
            boolean checkFolder = true; //Folder để in kết quả có tồn tại hay không
            if (!dirTestCase.exists()) {
                checkFolder = dirTestCase.mkdir();
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
                if(checkFolder){
                    String fitnessFile = subFolder + (seed + 1) + ".txt";
                    DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
                    for(int i=0;i<result.size();i++){
                        outFit.writeBytes(result.get(i));
                    }
                    outFit.close();
                }else {
                    System.err.print("Không thể tạo folder chứa data\n");
                }
                result.clear();
            }
        }
    }
}