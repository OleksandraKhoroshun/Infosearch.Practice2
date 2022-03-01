import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Index {

    ArrayList<String> doc_names;
     HashMap<String, ArrayList<Integer>> matrix;

     //constructor
    public Index(String folder){
        matrix = new HashMap<>();
        doc_names = new ArrayList();

        File dir = new File(folder);
        File[] files = dir.listFiles();

        int doc=-1;
        for (File file : files) {
            doc++;
            doc_names.add(file.getName());
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
            ArrayList<Integer> arr = new ArrayList<>();
            arr.add(doc);
            matrix.put(word,arr);
        }
        else{
            if(!matrix.get(word).contains(doc))
                matrix.get(word).add(doc);
            }
    }

    //boolean search
    public ArrayList<Integer> search(String input) throws Exception {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+","");

        char and = '&', or ='∨', not ='!';
        if(!input.matches("(("+not+")?[\\w]+((("+and+")|("+or+"))("+not+")?[\\w]+)*)"))
            throw new Exception("Incorrect format.");

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

        ArrayList<Integer> res = copy(matrix.get(words[0]));
        if(res==null)res = new ArrayList();
        if(nots[0]==1) res = swap(res);

        for(int i =1;i<words.length;i++){
            if(res==null)res = new ArrayList();

            ArrayList<Integer> temp = copy(matrix.get(words[i]));
            if(temp==null)temp = new ArrayList();

            ArrayList<Integer> ans = new ArrayList();
             if(nots[i] ==1) temp = swap(temp);

            if(operators[i-1].equals("&")){

                for(int j : res){
                    if(temp.contains(j)) ans.add(j);
                }
                res=ans;
            }
            else if (operators[i-1].equals("∨")){

                for(int j : res){
                    ans.add(j);
                }

                for(int j : temp){
                    if(!ans.contains(j)) ans.add(j);
                }
                res=ans;
            }
        }
        return res;
    }

    //check operators
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
    private ArrayList<Integer> swap(ArrayList<Integer> arr){
        ArrayList<Integer> result=new ArrayList();
        for(int i=0;i<doc_names.size();i++){
            if(!arr.contains(i)){
                result.add(i);
            }
        }
        return result;
    }

    private ArrayList<Integer> copy(ArrayList<Integer> arr){
        ArrayList<Integer> result=new ArrayList();
        for(int i=0;i<doc_names.size();i++){
            if(arr.contains(i)){
                result.add(i);
            }
        }
        return result;
    }

    //to string
    public void print() throws IOException {
        OutputStream out = new BufferedOutputStream( System.out );

        ArrayList<String> res_list = new ArrayList(matrix.keySet());
        Collections.sort(res_list);

        for(String s: res_list){
            out.write((s+" ").getBytes());
            for(int i=0;i<matrix.get(s).size();i++){
                int num = matrix.get(s).get(i)+1;
                out.write((num+" ").getBytes());
            }
            out.write(("\n ").getBytes());
        }
    }

}
