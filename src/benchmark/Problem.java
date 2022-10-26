package benchmark;

import basic.Params;

import java.util.ArrayList;

import static IO.DataIO.readDataTSP;

public class Problem {
    public ArrayList<Graph> graphs;
    public ArrayList<int[]> testCase;

    public Problem(){
        graphs = new ArrayList<>();
        graphs.add(readDataTSP(Params.linkData+"pr76.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr107.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr124.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr136.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr144.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr152.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr226.tsp"));
        graphs.add(readDataTSP(Params.linkData+"pr264.tsp"));

        testCase = new ArrayList<>();
        testCase.add(new int[]{0,1,2,3});
        testCase.add(new int[]{4,5,6,7});
        testCase.add(new int[]{0,1,6,7});
        testCase.add(new int[]{2,3,4,5});
        testCase.add(new int[]{0,2,3,6});
        testCase.add(new int[]{1,4,5,7});
        testCase.add(new int[]{0,1,3,5});
        testCase.add(new int[]{2,4,6,7});
        testCase.add(new int[]{0,3,4,6});
        testCase.add(new int[]{1,2,5,7});

        testCase.add(new int[]{0,1,2,3,4,5});
        testCase.add(new int[]{2,3,4,5,6,7});
        testCase.add(new int[]{0,1,4,5,6,7});
        testCase.add(new int[]{0,1,3,4,6,7});

        testCase.add(new int[]{0,1,2,3,4,5,6,7});

        for(Graph g: graphs){
            if(Params.maxTotalVertices < g.totalVertices)
                Params.maxTotalVertices = g.totalVertices;
        }
        Params.kmax = (int) Params.maxTotalVertices/10;
    }
}
