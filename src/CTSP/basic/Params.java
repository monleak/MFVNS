package CTSP.basic;

import java.util.Random;

public class Params {
    public static int type = 2; // 1-Large  2-Small
    public static String linkProb = "Data/b4gcgybvt6-4/Euclidean instances/Small Instances/Type_1_Small";
    public static String linkData = "Data//";
    public static String linkOutput = "Result//";
    public static String linkOutputCTSP = "CTSP//";
    public static String linkLarge = "Large//";
    public static String linkSmall = "Small//";
    public static int POP_SIZE = 20;
    public static int kmax = 4;
    public static Random rand; //Được khởi tạo theo các seed
    public static double countEvals; //Đếm số lần đánh giá
    public static int recordsNum; //Số lần ghi lại kết quả
    public static final int REPT = 30; //Số lần chạy
    public static int maxEvals;
    public static final int MAX_EVALS_PER_TESTCASE = 500000;
    public static int maxGeneration = 200;
    public static double bigNumber = 10000000;
    public static final double C = 0.02;
    public static int mindif = 5; //Độ sai khác tối thiểu giữa 2 gen
    public static int maxProb = 25; //lần lặp
}
