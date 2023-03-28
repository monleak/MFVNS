package CTSP.IO;

import CTSP.benchmark.Graph;
import CTSP.benchmark.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

enum typeGraph {
    EUCLIDEAN,NONEUCLIDEAN,NOTYPE;
}
public class DataIO {
    public static Graph readDataCTSP(String linkFile) {
        String startMess = "Reading data from "+linkFile;
        System.out.print(startMess);
        Graph graph = null;
        //Khởi tạo cấu trúc dữ liệu và lưu nó trong mảng 2 chiều
        BufferedReader readBuffer = null;
        try{
            readBuffer = new BufferedReader(new FileReader(linkFile));
            String line;
            typeGraph type = typeGraph.NOTYPE;
            do{
                //Đọc từng dòng của file đến phần bắt đầu tọa độ
                line = readBuffer.readLine();

                //Nhận type của file
                if(line.contains("NON_EUC_CLUSTERED_TREE")) {
                    type = typeGraph.NONEUCLIDEAN;
                }else if(line.contains("CLUSTERED_TREE")) {
                    type = typeGraph.EUCLIDEAN;
                }else if(line.contains("DIMENSION")){
                    //Đọc giá trị của tổng số thành phố
                    String[] result = line.split(":");
                    graph = new Graph(Integer.parseInt(result[1].trim()),linkFile);
                }else if(line.contains("NUMBER_OF_CLUSTERS")){
                    String[] result = line.split(":");
                    graph.numberOfCluster = Integer.parseInt(result[1].trim());
                }else if(line.contains("NODE_COORD_SECTION") || line.contains("EDGE_WEIGHT_SECTION"))
                    break; //Thoát khỏi vòng lặp
            }while (true);

            if(type == typeGraph.EUCLIDEAN){
                do{
                    //Đọc dữ liệu tọa độ đỉnh
                    line = readBuffer.readLine();
                    if(line.contains("CLUSTER_SECTION")){//Dấu hiệu bắt đầu của phần dữ liệu cụm
                        break;
                    }

                    String[] result = line.split(" ");
                    Vertex vertex = new Vertex(Integer.parseInt(result[0].trim()), Integer.parseInt(result[1].trim()), Integer.parseInt(result[3].trim()));
                    assert graph != null;
                    graph.addVertex(vertex);
                }while (true);

                for(int i = 0; i< graph.totalVertices; i++){
                    //Tính khoảng cách giữa các đỉnh là lưu lại
                    for (int j=0;j<graph.totalVertices;j++){
                        if(graph.distance[j][i] != 0) {
                            graph.distance[i][j] = graph.distance[j][i];
                            continue;
                        }
                        double dis = calDis(graph.vertexList.get(i).x,graph.vertexList.get(i).y,graph.vertexList.get(j).x,graph.vertexList.get(j).y);
                        graph.distance[i][j] = dis;
                    }
                }
            } else if(type == typeGraph.NONEUCLIDEAN){
                throw new Exception("\nHiện tại chưa hỗ trợ các bộ dữ liệu NONEUCLIDEAN");
//                for(int i=0;i< graph.totalVertices;i++){
//                    Vertex vertex = new Vertex(i+1);
//                    graph.addVertex(vertex);
//                }
//                int count = 0;
//                do{
//                    line = readBuffer.readLine();
//                    if(line.contains("CLUSTER_SECTION")){//Dấu hiệu bắt đầu của phần dữ liệu cụm
//                        break;
//                    }
//                    String[] result = line.split("\t");
//
//                    for (int j=0;j<graph.totalVertices;j++){
//                        graph.distance[count][j] = Integer.parseInt(result[j].trim());
//                    }
//                    count++;
//                }while (true);
            }

            line = readBuffer.readLine();
            if(line.contains("SOURCE_VERTEX")){ //Đọc đỉnh nguồn
                String[] result = line.split(":");
                graph.sourceVertex = Integer.parseInt(result[1].trim());
            }

            do{
                //Đọc dữ liệu cluster
                line = readBuffer.readLine();
                if(line.contains("EOF")){
                    break;
                }
                String[] result = line.split(" ");
                for(int j=1;j<result.length-1;j++){
                    graph.vertexList.get(Integer.parseInt(result[j].trim())).idCluster = Integer.parseInt(result[0].trim());
                }
            }while(true);
        }catch(Exception e){
            System.out.println(e);
            return graph;
        }
        String clearStartMess = "\b".repeat(startMess.length());
        System.out.print(clearStartMess);
        System.out.println(startMess + " ✔️DONE");
        return graph;
    }
    public static double calDis(int x1,int y1,int x2,int y2){
        //hàm tính khoảng cách
        double n;
        n = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        return n;
    }
}
