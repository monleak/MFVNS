package OCSTP.util;

import OCSTP.basic.Individual;
import OCSTP.benchmark.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class utilOCSTP {
    public static double calCost(Individual indiv, Graph graph){
        ArrayList<Integer> IDVertexUsed = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tree = new ArrayList<>();
        for(int d = 0; d <= graph.totalVertices; d++) {
            tree.add(new ArrayList<Integer>());
        }

        ArrayList<Double> chomosome =  new ArrayList<>();
        for(var d : indiv.Chromosome){
            chomosome.add(d);
        }
        List<Integer> edges = util.sortIndex(chomosome);
        int totalEdges = graph.totalVertices*(graph.totalVertices-1)/2;
        for(var edge : edges){ //edge tinh tu 0
            if(edge+1 > totalEdges){
                continue;
            }
            int[] rowCol = getIDVertexFromPointEdge(edge+1, graph.totalVertices);
            if(IDVertexUsed.size() == 0
              || (IDVertexUsed.contains(rowCol[0]) && !IDVertexUsed.contains(rowCol[1]))
              || (IDVertexUsed.contains(rowCol[1]) && !IDVertexUsed.contains(rowCol[0]))
            ){
                if(!IDVertexUsed.contains(rowCol[0])){
                    IDVertexUsed.add(rowCol[0]);
                }else if(!IDVertexUsed.contains(rowCol[1])){
                    IDVertexUsed.add(rowCol[1]);
                }
                tree.get(rowCol[1]).add(rowCol[0]);
                tree.get(rowCol[0]).add(rowCol[1]);
            }
            if (IDVertexUsed.size() >= graph.totalVertices){
                break;
            }
        }
        double costAll = 0;
        for (int i = 0; i < graph.reqs.length; i++) {
            for (int j = 0; j < graph.reqs[i].length; j++) {
                if(graph.reqs[i][j] != 0){
                    ArrayList<Integer> path = new ArrayList<>();
                    find_path(i+1, j+1, new ArrayList<>(), tree,path);

                    double costPath = 0;
                    //cal cost
                    for (int k = 0; k < path.size()-1; k++) {
                        costPath += graph.distance[path.get(k)-1][path.get(k+1)-1];
                    }
                    costAll += costPath*graph.reqs[i][j];
                }
            }
        }
        return costAll;
    }

    /**
     *
     * @param point - tinh tu 1, la vi tri trong gen
     * @param totalVertices
     * @return row,col
     */
    public static int[] getIDVertexFromPointEdge(int point, int totalVertices){
        int totalEdge = totalVertices*(totalVertices-1)/2;
        int col = point,row=1;
        while (col > (totalVertices-row)){
            col = col - (totalVertices-row);
            row++;
        }
        col+=row;
        return new int[]{row,col};
    }

    public static void DFS(int start, int end, ArrayList<ArrayList<Integer>> tree){

    }

    private static void DFS(boolean vis[], int x, int y, ArrayList<Integer> stack, ArrayList<ArrayList<Integer>> v,ArrayList<Integer> path) {

        stack.add(x);
        if (x == y)
        {
            for(int ii : stack)
                path.add(ii);
            return;
        }
        vis[x] = true;

        // if backtracking is taking place
        if (v.get(x).size() > 0)
        {
            for(int j = 0; j < v.get(x).size(); j++)
            {

                // if the node is not visited
                if (vis[v.get(x).get(j)] == false)
                {
                    DFS(vis, v.get(x).get(j), y, stack,v,path);
                }
            }
        }

        stack.remove(stack.size() - 1);
    }

    private static void find_path(int x, int y, ArrayList<Integer> stack,ArrayList<ArrayList<Integer>> T,ArrayList<Integer> path) {
        boolean vis[] = new boolean[T.size() + 1];
        Arrays.fill(vis, false);
        DFS(vis, x, y, stack,T,path);
    }
}
