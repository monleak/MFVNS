package CTSP.benchmark;

import CTSP.basic.Params;

import java.util.ArrayList;

import static CTSP.IO.DataIO.readDataTSP;

public class Problem {
    public ArrayList<Graph> graphs;
    public ArrayList<int[]> testCase;

    public Problem(){
        graphs = new ArrayList<>();
        graphs.add(readDataTSP(Params.linkData+"pr76.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(108159);
        graphs.add(readDataTSP(Params.linkData+"pr107.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(44303);
        graphs.add(readDataTSP(Params.linkData+"pr124.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(59030);
        graphs.add(readDataTSP(Params.linkData+"pr136.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(96772);
        graphs.add(readDataTSP(Params.linkData+"pr144.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(58537);
        graphs.add(readDataTSP(Params.linkData+"pr152.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(73682);
        graphs.add(readDataTSP(Params.linkData+"pr226.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(80369);
        graphs.add(readDataTSP(Params.linkData+"pr264.CTSP"));
        graphs.get(graphs.size()-1).setOptimal(49135);

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
    }
}
