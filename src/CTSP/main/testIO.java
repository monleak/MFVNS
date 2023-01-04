package CTSP.main;

import CTSP.benchmark.Graph;

import java.io.IOException;

import static CTSP.IO.DataIO.readDataCTSP;

public class testIO {
    public static void main(String[] args){
        Graph graph = readDataCTSP("/home/monleak/Code/MFVNS/Data/b4gcgybvt6-4/Non-Euclidean Instances/ Small Instances/Type_1_Small/5berlin52.clt");
    }
}
