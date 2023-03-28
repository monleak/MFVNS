package CTSP.main;

import CTSP.benchmark.Graph;

import java.io.IOException;

import static CTSP.IO.DataIO.readDataCTSP;

public class testIO {
    public static void main(String[] args){
        Graph graph = readDataCTSP("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large/10a280.clt");
    }
}
