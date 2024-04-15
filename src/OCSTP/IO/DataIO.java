package OCSTP.IO;

import OCSTP.benchmark.Cluster;
import OCSTP.benchmark.Graph;
import OCSTP.benchmark.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

enum typeGraph {
    EUCLIDEAN,NONEUCLIDEAN,NOTYPE;
}
enum typeFile {
    CLT,HTSP;
}
public class DataIO {
//    public static Graph readDataCTSP(String linkFile, ArrayList<String> orderTask, typeFile typeFile) {
//        String startMess = "Reading data from "+linkFile;
//        System.out.print(startMess);
//        Graph graph = null;
//
//        //Khởi tạo cấu trúc dữ liệu và lưu nó trong mảng 2 chiều
//        BufferedReader readBuffer = null;
//        typeGraph type = typeGraph.NOTYPE;
//        if(typeFile == OCSTP.IO.typeFile.CLT){
//            try{
//                readBuffer = new BufferedReader(new FileReader(linkFile));
//                String line;
//                int totalVertices = 0;
//                int numberOfCluster = 0;
//                do{
//                    //Đọc từng dòng của file đến phần bắt đầu tọa độ
//                    line = readBuffer.readLine();
//
//                    //Nhận type của file
//                    if(line.contains("NON_EUC_CLUSTERED_TREE")) {
//                        type = typeGraph.NONEUCLIDEAN;
//                    }else if(line.contains("CLUSTERED_TREE")) {
//                        type = typeGraph.EUCLIDEAN;
//                    }else if(line.contains("DIMENSION")){
//                        //Đọc giá trị của tổng số thành phố
//                        String[] result = line.split(":");
//                        totalVertices = Integer.parseInt(result[1].trim());
//                    }else if(line.contains("NUMBER_OF_CLUSTERS")){
//                        String[] result = line.split(":");
//                        numberOfCluster = Integer.parseInt(result[1].trim());
//                    }else if(line.contains("NODE_COORD_SECTION") || line.contains("EDGE_WEIGHT_SECTION"))
//                        break; //Thoát khỏi vòng lặp
//                }while (true);
//
//                graph = new Graph(totalVertices,numberOfCluster,linkFile);
//
//                if(type == typeGraph.EUCLIDEAN){
//                    do{
//                        //Đọc dữ liệu tọa độ đỉnh
//                        line = readBuffer.readLine();
//                        if(line.contains("CLUSTER_SECTION")){//Dấu hiệu bắt đầu của phần dữ liệu cụm
//                            break;
//                        }
//
//                        String[] result = line.split(" ");
//                        Vertex vertex = new Vertex(Integer.parseInt(result[0].trim()), Integer.parseInt(result[1].trim()), Integer.parseInt(result[3].trim()));
//                        graph.addVertex(vertex);
//                    }while (true);
//
//                    for(int i = 0; i< graph.totalVertices; i++){
//                        //Tính khoảng cách giữa các đỉnh là lưu lại
//                        for (int j=0;j<graph.totalVertices;j++){
//                            if(graph.distance[j][i] != 0) {
//                                graph.distance[i][j] = graph.distance[j][i];
//                                continue;
//                            }
//                            double dis = calDis(graph.vertexList.get(i).x,graph.vertexList.get(i).y,graph.vertexList.get(j).x,graph.vertexList.get(j).y);
//                            graph.distance[i][j] = dis;
//                        }
//                    }
//                } else if(type == typeGraph.NONEUCLIDEAN){
//                    for(int i=0;i< graph.totalVertices;i++){
//                        Vertex vertex = new Vertex(i+1,0,0);
//                        graph.addVertex(vertex);
//                    }
//                    int count = 0;
//                    do{
//                        line = readBuffer.readLine();
//                        if(line.contains("CLUSTER_SECTION")){//Dấu hiệu bắt đầu của phần dữ liệu cụm
//                            break;
//                        }
//                        String[] result = line.split("\t");
//
//                        for (int j=0;j<graph.totalVertices;j++){
//                            graph.distance[count][j] = Integer.parseInt(result[j].trim());
//                        }
//                        count++;
//                    }while (true);
//                }
//
//                line = readBuffer.readLine();
//                if(line.contains("SOURCE_VERTEX")){ //Đọc đỉnh nguồn
//                    String[] result = line.split(":");
//                    graph.sourceVertex = Integer.parseInt(result[1].trim());
//                }
//
//                do{
//                    //Đọc dữ liệu cluster
//                    line = readBuffer.readLine();
//                    if(line.contains("EOF")){
//                        break;
//                    }
//                    String[] result = line.split(" ");
//                    for(int j=1;j<result.length-1;j++){
//                        graph.addVertexToCluster(Integer.parseInt(result[0].trim())-1,Integer.parseInt(result[j].trim()));
//                    }
//                }while(true);
//
//                readBuffer.close();
//            }catch(Exception e){
//                System.out.println(" ❌FAIL");
//                orderTask.add(startMess+" ❌FAIL\n");
//                return graph;
//            }
//        }else if (typeFile == OCSTP.IO.typeFile.HTSP){
//            try{
//                readBuffer = new BufferedReader(new FileReader(linkFile));
//                String line = readBuffer.readLine();
//                int totalVertices = Integer.parseInt(line); //đọc tổng số đỉnh
//                line = readBuffer.readLine();
//                int numberOfCluster = Integer.parseInt(line.split(" ")[0]); //Đọc tổng số cluster
//                graph = new Graph(totalVertices,numberOfCluster,linkFile);
//                for(int i=0;i<totalVertices;i++){
//                    graph.addVertex(new Vertex(i+1,0,0));
//                    line = readBuffer.readLine();
//                    graph.distance[i] = Arrays.stream(line.split(" "))
//                            .mapToDouble(Double::parseDouble)
//                            .toArray();
//                }
//                for(int i=0;i<numberOfCluster;i++){
//                    line = readBuffer.readLine();
//                    var temp = line.split(" ");
//                    for(int j = 2; j < temp.length;j++){
//                        graph.addVertexToCluster(i,Integer.parseInt(temp[j])-1);
//                    }
//                }
//                readBuffer.close();
//            }catch (Exception e){
//                System.out.println(" ❌FAIL");
//                orderTask.add(startMess+" ❌FAIL\n");
//                return graph;
//            }
//        }
//        if(graph != null){
//            graph.sortListCluster(); //Gán nhãn lại các cluster theo số lượng đỉnh tăng dần
//            if(type == typeGraph.EUCLIDEAN){
//                for (int i=0;i<graph.listCluster.size();i++){
//                    graph.listCluster.get(i).sortListVertex(); //Gán nhãn lại các đỉnh trong cluster theo khoảng cách đến O tăng dần
//                }
//            }
//            graph.calPrivateSpace();
//        }
//        System.out.println(" ✔️DONE");
//        orderTask.add(startMess+" ✔️DONE\n");
//        return graph;
//    }

    public static Graph readDataOCSTP(String linkFile, ArrayList<String> orderTask){
        String startMess = "Reading data from "+linkFile;
        System.out.print(startMess);
        Graph graph = null;
        //Khởi tạo cấu trúc dữ liệu và lưu nó trong mảng 2 chiều
        BufferedReader readBuffer = null;
        try{
            readBuffer = new BufferedReader(new FileReader(linkFile));
            String line;
            int totalVertices = Integer.parseInt(readBuffer.readLine());

            graph = new Graph(totalVertices,linkFile);

            for(int i=0;i< totalVertices;i++){
                String[] dataLine = readBuffer.readLine().split("\t");
                for(int j=0;j<totalVertices;j++){
                    graph.distance[i][j] = Double.parseDouble(dataLine[j]);
                }
            }
            for(int i=0;i< totalVertices;i++){
                String[] dataLine = readBuffer.readLine().split("\t");
                for(int j=0;j<totalVertices;j++){
                    graph.reqs[i][j] = Double.parseDouble(dataLine[j]);
                }
            }
            readBuffer.close();
        }catch(Exception e){
            System.out.println(" ❌FAIL "+e.getMessage());
            orderTask.add(startMess+" ❌FAIL\n");
            return graph;
        }
        System.out.println(" ✔️DONE");
        orderTask.add(startMess+" ✔️DONE\n");
        return graph;
    }
    public static double calDis(int x1,int y1,int x2,int y2){
        //hàm tính khoảng cách
        double n;
        n = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        return n;
    }
}
