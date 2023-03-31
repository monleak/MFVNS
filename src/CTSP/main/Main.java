package CTSP.main;

import CTSP.basic.Params;
import CTSP.benchmark.Problem;
import CTSP.core.MFVNS;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
    /*
        Tạo các thư mục chứa kết quả
     */
        File dir = new File(Params.linkOutput);
        if (!dir.exists()) {
            boolean checkDirIsExists = dir.mkdir();
            if(!checkDirIsExists){
                System.out.println("Lỗi khi tạo folder Result");
            }
        }
        File subDir = new File(Params.linkOutput+Params.linkOutputCTSP); //Tạo thư mục con chứa kết quả CTSP nếu chưa có
        if (!subDir.exists()) {
            boolean checkSubDirIsExists = subDir.mkdir();
            if(!checkSubDirIsExists){
                System.out.println("Lỗi khi tạo folder Result/CTSP");
            }
        }

        /*
        LARGE INSTANCES
         */
        File largeInstance = new File(Params.linkOutput+Params.linkOutputCTSP+"Large//"); //Tạo thư mục con chứa kết quả Large instance nếu chưa có
        if (!largeInstance.exists()) {
            boolean check = largeInstance.mkdir();
            if(!check){
                System.out.println("Lỗi khi tạo folder Result/CTSP/Large/");
            }
        }
        ArrayList<Problem> largeProblem = new ArrayList<>();
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large"));
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_2"));
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_3_Large"));
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_4_Large"));
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_5_Large"));
        largeProblem.add(new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_6_Large"));

        for(int problemID=0;problemID<largeProblem.size();problemID++){
            File typeDir = new File(Params.linkOutput+Params.linkOutputCTSP+"Large//"+largeProblem.get(problemID).instanceName);
            if (!typeDir.exists()) {
                boolean check = typeDir.mkdir();
                if(!check){
                    System.out.println("Lỗi khi tạo folder Result/CTSP/Large/"+largeProblem.get(problemID).instanceName);
                }
            }

            for (int seed = 0; seed < Params.REPT; seed++){
                ArrayList<String> result = new ArrayList<>();
                System.out.println("=============== " + largeProblem.get(problemID).instanceName +" Seed "+ seed +" =================");
                Params.rand = new Random(seed);
                Params.countEvals = 0;
                Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
                MFVNS solver = new MFVNS(prob,testCase);
                solver.run(result);

                //in ra file
                String fitnessFile = typeDir.getPath() + (seed + 1) + ".txt";
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