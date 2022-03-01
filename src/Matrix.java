import java.io.*;
import java.util.*;

public class Matrix {

    int init_capacity;
    String[] doc_names;
    HashMap<String, byte[]> matrix;

    //constructor
    public Matrix(String folder){
        matrix = new HashMap<>();

        File dir = new File(folder);
        File[] files = dir.listFiles();
        init_capacity = files.length;

        int doc=-1;
        for (File file : files) {
            doc++;
            if(file.isFile()) {
                BufferedReader br = null;
                String line;
                try {
                    br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        addWords(doc,line);
                    }
                }catch(IOException e) {
                    System.out.println(e);
                }
                finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        doc_names = new String [doc+1];
        for(int i=0;i<doc+1;i++){
            doc_names[i]=files[i].getName();
        }
    }

    //checking and adding words
    public void addWords(int doc, String line){
        if(line.equals("")) return;
        String[] temp = line.split("[^a-zA-Z0-9_]+");
        for(int i=0;i<temp.length;i++){
            if(temp[i].matches("[a-zA-Z0-9_]+")) {
                addWord(temp[i].toLowerCase(),doc);
            }
        }
    }

    //adding words to hashmap
    public void addWord(String word,int doc){
        if(!matrix.containsKey(word)){
            byte [] arr = new byte [init_capacity];
            arr[doc] = 1;
            matrix.put(word,arr);
        }
        else{
            if(matrix.get(word).length<=doc){
                resize(matrix.get(word));
            }
            else{
                matrix.get(word)[doc] = 1;
            }

        }
    }

    //to string
    public void print() throws IOException {
        OutputStream out = new BufferedOutputStream( System.out );

        ArrayList<String> res_list = new ArrayList(matrix.keySet());
        Collections.sort(res_list);
        for(int i=1;i<doc_names.length+1;i++){
                out.write((i+" ").getBytes());
        }
            out.write(("\n").getBytes());
        for(String s: res_list){
            for(int i=0;i<doc_names.length;i++){
                    out.write((matrix.get(s)[i]+" ").getBytes());
            }
            out.write((s+" "+"\n").getBytes());
        }

    }

    //make size x2 bigger
    private void resize(byte[] arr) {
        byte[] temp = new byte[arr.length*2];

        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i];
        }
        arr = temp;
    }

    //boolean search
    public byte[] search(String input) throws Exception {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+","");

        char and = '&', or ='∨', not ='!';
        if(!input.matches("(("+not+")?[\\w]+((("+and+")|("+or+"))("+not+")?[\\w]+)*)"))
            throw new Exception("Incorrect format. ");

        String [] words = input.split("(("+and+")|("+or+"))");
        String[] operators = input.split("[^&∨]");
        operators = check(operators);

        byte [] nots = new byte [words.length];
        for(int i=0;i<nots.length;i++){
            if(words[i].charAt(0)==not){
                words[i] = words[i].replaceAll("!","");
                nots[i]=1;
            }
        }

        byte [] res = copy(matrix.get(words[0]));
        if(res==null)res=new byte[doc_names.length];
        if(nots[0]==1)res = swap(res);


        for(int i =1;i<words.length;i++){
            if(res==null)res=new byte[doc_names.length];

            byte[] temp = copy(matrix.get(words[i]));
            if(temp==null)temp=new byte[doc_names.length];

            if(nots[i]==1) temp = swap(temp);

                for(int j =0;j< doc_names.length;j++) {
                    if((operators[i-1].equals("&"))){
                        if(res[j]==1 && temp[j]==1){
                            res[j]=1;
                        }
                        else res[j]=0;
                    } else if(operators[i-1].equals("∨")){
                        if(res[j]==1 || temp[j]==1){
                            res[j]=1;
                        }
                        else res[j]=0;

                    }else res[j]=0;

                }
        }
        return res;
    }

    //checking if operators are ok
    private String [] check(String [] arr){
        ArrayList<String> temp=new ArrayList();
        int j=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals("&") || arr[i].equals("∨")){
                temp.add(j,arr[i]);
                j++;
            }
        }
        String [] res = new String [j];
        res = temp.toArray(res);
        return res;
    }

    //not array
    private byte[] swap(byte[] arr){
        byte [] res = new byte[arr.length];
        for(int i=0;i<arr.length;i++){
            res[i] = (byte) (1-arr[i]);
        }
        return res;
    }

    private byte[] copy(byte[] arr){
        byte [] res = new byte[arr.length];
        for(int i=0;i<arr.length;i++){
            res[i] = arr[i];
        }
        return res;
    }

}
