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

        File largeInstance = new File(Params.linkOutput+Params.linkOutputCTSP+(Params.type == 1 ? Params.linkLarge : Params.linkSmall)); //Tạo thư mục con chứa kết quả Large instance nếu chưa có
        if (!largeInstance.exists()) {
            boolean check = largeInstance.mkdir();
            if(!check){
                System.out.println("Lỗi khi tạo folder Result/CTSP/"+(Params.type == 1 ? Params.linkLarge : Params.linkSmall));
            }
        }

        //Ghi thứ tự các task vào 1 file
        ArrayList<String> orderTask = new ArrayList<>();
        Problem newProb = new Problem(Params.linkProb,orderTask);

        File typeDir = new File(Params.linkOutput+Params.linkOutputCTSP+(Params.type == 1 ? Params.linkLarge : Params.linkSmall)+newProb.instanceName);
        if (!typeDir.exists()) {
            boolean check = typeDir.mkdir();
            if(!check){
                System.out.println("Lỗi khi tạo folder Result/CTSP/"+(Params.type == 1 ? Params.linkLarge : Params.linkSmall)+newProb.instanceName);
            }
        }

        //in ra file
        String orderTaskFile = typeDir.getPath()+"//"+ newProb.instanceName+"_ORDER-TASK.txt";
        DataOutputStream outOrderTask = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(orderTaskFile)));
        for(int i=0;i<orderTask.size();i++){
            outOrderTask.writeBytes(orderTask.get(i));
        }
        orderTask.clear();
        outOrderTask.close();

        //Hoạt động chính
        for (int seed = 0; seed < Params.REPT; seed++){
            ArrayList<String> result = new ArrayList<>();
            System.out.println("=============== " + newProb.instanceName +" Seed "+ seed +" =================");
            Params.rand = new Random(seed);
            Params.countEvals = 0;
            Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
            MFVNS solver = new MFVNS(newProb);
            solver.run(result);

            //in ra file
            String fitnessFile = typeDir.getPath()+"//"+ newProb.instanceName+"_SEED" + (seed + 1) + ".txt";
            DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
            for(int i=0;i<result.size();i++){
                outFit.writeBytes(result.get(i));
            }
            result.clear();
            outFit.close();
        }

    }
}