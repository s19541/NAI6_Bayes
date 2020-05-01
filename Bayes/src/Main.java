public class Main {
    public static void main(String[] args) {
        BayesClassificator bayesClassificator=new BayesClassificator("trainingset.csv");
        bayesClassificator.classifyFromFile("testset.csv");
    }
}
