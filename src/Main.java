import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

       long time_m =  System.nanoTime();
        Matrix m = new Matrix("C:\\Users\\alena\\IdeaProjects\\Practice1.Vocabulary\\Collection");
        time_m = System.nanoTime()-time_m;
        long time_i =  System.nanoTime();
        Index ind = new Index("C:\\Users\\alena\\IdeaProjects\\Practice1.Vocabulary\\Collection");
        time_i = System.nanoTime()-time_i;

        System.out.println("\n1.Show matrix;\n2.Show index;\n3.Show time for matrix;\n4.Show time for index" +
                ";\n5.Search matrix;\n6.Search input;\n-1.Exit\n");
        int i= in.nextInt();

        while(i!=-1) {
            switch (i) {
                case 1:
                    m.print();
                    break;
                case 2:
                    ind.print();
                    break;
                case 3:
                    System.out.println("time: " + time_m / 1000000000 + " s");
                    break;
                case 4:
                    System.out.println("time: " + time_i / 1000000000 + " s");
                    break;
                case 5:
                    System.out.println("Enter:");
                    in.nextLine();
                    String input = in.nextLine();
                    byte[] res = m.search(input);
                    System.out.printf(input+": ");
                    print(res);
                    break;
                case 6:
                    System.out.println("Enter:");
                    in.nextLine();
                    input = in.nextLine();
                    ArrayList<Integer> res_list = ind.search(input);
                    System.out.printf(input+": "+res_list+"\n");
                    break;
                default:
                    System.out.println("Wrong format");
            }
            System.out.println("\n1.Show matrix;\n2.Show index;\n3.Show time for matrix;\n4.Show time for index" +
                    ";\n5.Search matrix;\n6.Search input;\n-1.Exit\n");
            i = in.nextInt();
        }


    }
    //print byte array
    public static void print(byte[] res){
        for(int i=0;i<res.length;i++)
            System.out.print(res[i]+" ");
        System.out.println();
    }
}
