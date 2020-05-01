import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BayesClassificator {
    private List<List<String>>trainData;
    public BayesClassificator(String trainSet){
        trainData=loadSetFromFile(trainSet);
    }
    private static List<List<String>> loadSetFromFile(String filePath){
        List<List<String>> set=new ArrayList<>();
        try{
            BufferedReader bf=new BufferedReader(new FileReader(filePath));
            String line;
            while((line=bf.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                String token;
                List<String>attrs=new ArrayList<>();
                while(tokenizer.hasMoreTokens()){
                    token=tokenizer.nextToken();
                    attrs.add(token);
                }
                set.add(attrs);
            }
            bf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return set;
    }
    void classifyFromFile(String filePath){
        List<List<String>>testData=loadSetFromFile(filePath);
        for(List<String>object:testData) {
            classify(object);
            System.out.println();
        }
    }
    private void classify(List<String>attrs){
        List<DecisionalAttribute>decisionalAttributes=findDecisionalAttributes();
        for(DecisionalAttribute decisionalAttribute:decisionalAttributes){
            double possibility = countPossibility(attrs,decisionalAttribute);
            decisionalAttribute.setPossibility(possibility);
        }
        System.out.print("Attributes: ");
        for(String attr:attrs){
            System.out.print(attr+" ");
        }
        System.out.print("classified to: "+findDecisionalAttributeWithLowestPossibility(decisionalAttributes));
    }
    private List<DecisionalAttribute>findDecisionalAttributes(){
        List<DecisionalAttribute>appearedPossibilites=new ArrayList<>();
        for(List<String>object:trainData){
            Boolean appeared=false;
            String value=object.get(trainData.get(0).size()-1);
            for(DecisionalAttribute appearedValue:appearedPossibilites){
                if(value.equals(appearedValue.getValue()))
                    appeared=true;
            }
            if(!appeared){
                appearedPossibilites.add(new DecisionalAttribute(value));
            }
        }
        return appearedPossibilites;
    }
    private double countPossibility(List<String>attrs,DecisionalAttribute decisionalAttribute){
        double possibility=countAppearanceOfValue(decisionalAttribute.getValue(),trainData.get(0).size()-1,decisionalAttribute)/trainData.size();
        for (int i = 0; i < attrs.size(); i++) {
            possibility*=((countAppearanceOfValue(attrs.get(i),i,decisionalAttribute)+1)/(trainData.size()+countNumberOfPossibilities(i)));
        }
        return possibility;
    }
    private double countAppearanceOfValue(String value,int attributeNumber,DecisionalAttribute decisionalAttribute){
        int counter=0;
        for(List<String>object:trainData){
            if(object.get(attributeNumber).equals(value)&&object.get(object.size()-1).equals(decisionalAttribute.getValue()))
                counter++;
        }
        return counter;
    }
    private double countNumberOfPossibilities(int attributeNumber){
        int counter=0;
        List<String>countedPossibilites=new ArrayList<>();
        for(List<String>object:trainData){
            Boolean appeared=false;
            String value=object.get(attributeNumber);
            for(String countedValue:countedPossibilites){
                if(value.equals(countedValue))
                    appeared=true;
            }
            if(!appeared){
                counter++;
                countedPossibilites.add(value);
            }
        }
        return counter;
    }
    private String findDecisionalAttributeWithLowestPossibility(List<DecisionalAttribute>decisionalAttributes){
        DecisionalAttribute retDecisionalAttribute=decisionalAttributes.get(0);
        for(DecisionalAttribute decisionalAttribute:decisionalAttributes){
            if(decisionalAttribute.getValue()!=retDecisionalAttribute.getValue()){
                if(decisionalAttribute.getPossibility()>retDecisionalAttribute.getPossibility())
                    retDecisionalAttribute=decisionalAttribute;
            }
        }
        return retDecisionalAttribute.getValue();
    }
}
