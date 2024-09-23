package uob.oop;

import java.text.DecimalFormat;


public class NewsClassifier {
    public String[] myHTMLs;
    public String[] myStopWords = new String[127];
    public String[] newsTitles;
    public String[] newsContents;
    public String[] newsCleanedContent;
    public double[][] newsTFIDF;

    private final String TITLE_GROUP1 = "Osiris-Rex's sample from asteroid Bennu will reveal secrets of our solar system";
    private final String TITLE_GROUP2 = "Bitcoin slides to five-month low amid wider sell-off";

    public Toolkit myTK;

    public NewsClassifier() {
        myTK = new Toolkit();
        myHTMLs = myTK.loadHTML();
        myStopWords = myTK.loadStopWords();

        loadData();
    }

    public static void main(String[] args) {
        NewsClassifier myNewsClassifier = new NewsClassifier();

        myNewsClassifier.newsCleanedContent = myNewsClassifier.preProcessing();

        myNewsClassifier.newsTFIDF = myNewsClassifier.calculateTFIDF(myNewsClassifier.newsCleanedContent);

        //Change the _index value to calculate similar based on a different news article.
        double[][] doubSimilarity = myNewsClassifier.newsSimilarity(0);

        System.out.println(myNewsClassifier.resultString(doubSimilarity, 10));

        String strGroupingResults = myNewsClassifier.groupingResults(myNewsClassifier.TITLE_GROUP1, myNewsClassifier.TITLE_GROUP2);
        System.out.println(strGroupingResults);
    }

    public void loadData() {
        //TODO 4.1 - 2 marks
        newsTitles = new String[myHTMLs.length];
        newsContents = new String[myHTMLs.length];
        for(int i = 0; i<myHTMLs.length; i++){
            newsTitles[i] = HtmlParser.getNewsTitle(myHTMLs[i]);
            newsContents[i] = HtmlParser.getNewsContent(myHTMLs[i]);
        }
    }

    public String[] preProcessing() {
        String[] myCleanedContent = new String[newsContents.length];
        //TODO 4.2 - 5 marks
        for(int i=0; i<newsContents.length;i++){
            String cleanedTemp = NLP.textCleaning(newsContents[i]);
            String lemmatizedTemp = NLP.textLemmatization(cleanedTemp);
            String stopRemovedTemp = NLP.removeStopWords(lemmatizedTemp, myStopWords);
            myCleanedContent[i] = stopRemovedTemp;

        }

        return myCleanedContent;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        double[][] myTFIDF = new double[_cleanedContents.length][vocabularyList.length];


        //TODO 4.3 - 10 marks

        double[] totalWordCounts = new double[vocabularyList.length];
        for(int i = 0; i<_cleanedContents.length; i++){
            String[] splitContent = _cleanedContents[i].split(" ");
            for(int j = 0; j<vocabularyList.length; j++){
                boolean flagFound = false;
                for(int k = 0; k<splitContent.length && flagFound == false; k++){
                    if(splitContent[k].equals(vocabularyList[j])){
                        totalWordCounts[j]+=1;
                        flagFound = true;
                    }
                }
            }
        }


        for(int i = 0; i< _cleanedContents.length; i++){
            String[] words = _cleanedContents[i].split(" ");
            for(int j = 0; j< vocabularyList.length; j++){
                double wordCounter = 0;
                for(String word : words) {
                    if (word.equals(vocabularyList[j])) {
                        wordCounter += 1;
                    }
                }
                double TF = wordCounter / words.length;
                double IDF = Math.log((double) _cleanedContents.length / totalWordCounts[j]) + 1;


                myTFIDF[i][j] = TF * IDF;
            }
        }

        return myTFIDF;
    }

    public String[] buildVocabulary(String[] _cleanedContents) {//Think is good
        String[] arrayVocabulary = null;

        //TODO 4.4 - 10 marks
        int maxSize = 100;
        int currentSize = 0;
        arrayVocabulary = new String[maxSize];

        for(int i = 0; i<_cleanedContents.length; i++){
            String[] splitContent = _cleanedContents[i].split(" ");
            for(int j = 0; j<splitContent.length; j++){
                boolean flagFound = false;
                for(int k = 0; k< currentSize; k++){
                    if(arrayVocabulary[k].equals(splitContent[j])){
                        flagFound = true;
                    }
                }
                if(!flagFound){
                    arrayVocabulary[currentSize] = splitContent[j];
                    currentSize++;
                    if(currentSize == maxSize){
                        maxSize *= 2;
                        String[] tempArray = new String[maxSize];
                        for(int k = 0; k<arrayVocabulary.length; k++){
                            tempArray[k] = arrayVocabulary[k];
                        }
                        arrayVocabulary = tempArray;
                    }
                }
            }
        }
        String[] tempArray = new String[currentSize];
        for(int i = 0; i<tempArray.length; i++){
            tempArray[i] = arrayVocabulary[i];
        }
        arrayVocabulary = tempArray;

        return arrayVocabulary;
    }

    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = new double[newsTFIDF.length][2];

        //TODO 4.5 - 15 marks
        Vector thisIndexVector = new Vector(newsTFIDF[_newsIndex]);


        for(int i = 0; i<newsTFIDF.length; i++){
            Vector comparedVector = new Vector(newsTFIDF[i]);
            mySimilarity[i] = new double[]{i, thisIndexVector.cosineSimilarity(comparedVector)};
        }


        boolean sorted = false;
        while(!sorted){
            boolean switched = false;
            for(int i = 0; i<mySimilarity.length-1; i++){

                if(mySimilarity[i][1] < mySimilarity[i+1][1]){
                    double[] temp = mySimilarity[i];
                    mySimilarity[i] = mySimilarity[i+1];
                    mySimilarity[i+1] = temp;
                    switched = true;
                }
            }
            if(!switched){
                sorted = true;
            }
        }

        return mySimilarity;
    }

    public String groupingResults(String _firstTitle, String _secondTitle) {
        int[] arrayGroup1 = new int[newsTFIDF.length], arrayGroup2 = new int[newsTFIDF.length];
        //TODO 4.6 - 15 marks
        int firstIndex = -1;
        int secondIndex = -1;
        for(int i=0; i< newsTitles.length; i++){
            if(newsTitles[i].equals(_firstTitle)){
                firstIndex = i;
            }
            else if(newsTitles[i].equals(_secondTitle)){
                secondIndex=i;
            }
        }
        double[] firstTFIDF = newsTFIDF[firstIndex];
        double[] secondTFIDF = newsTFIDF[secondIndex];

        Vector firstVector = new Vector(firstTFIDF);
        Vector secondVector = new Vector(secondTFIDF);
        int firstArrayGroupSize = 0;
        int secondArrayGroupSize = 0;
        for(int i = 0; i< newsTFIDF.length; i++){
            Vector indexVector = new Vector(newsTFIDF[i]);
            double firstCS = firstVector.cosineSimilarity(indexVector);
            double secondCS = secondVector.cosineSimilarity(indexVector);
            if(firstCS > secondCS){
                arrayGroup1[firstArrayGroupSize] = i;
                firstArrayGroupSize++;
            }
            else if(firstCS < secondCS){
                arrayGroup2[secondArrayGroupSize] = i;
                secondArrayGroupSize++;
            }
        }
        int[] firstTemp = new int[firstArrayGroupSize];
        int[] secondTemp = new int[secondArrayGroupSize];
        for(int i = 0; i<firstTemp.length; i++){
            firstTemp[i] = arrayGroup1[i];
        }
        for(int i = 0; i<secondTemp.length; i++){
            secondTemp[i] = arrayGroup2[i];
        }
        arrayGroup1 = firstTemp;
        arrayGroup2 = secondTemp;


        return resultString(arrayGroup1, arrayGroup2);
    }

    public String resultString(double[][] _similarityArray, int _groupNumber) {
        StringBuilder mySB = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        for (int j = 0; j < _groupNumber; j++) {
            for (int k = 0; k < _similarityArray[j].length; k++) {
                if (k == 0) {
                    mySB.append((int) _similarityArray[j][k]).append(" ");
                } else {
                    String formattedCS = decimalFormat.format(_similarityArray[j][k]);
                    mySB.append(formattedCS).append(" ");
                }
            }
            mySB.append(newsTitles[(int) _similarityArray[j][0]]).append("\r\n");
        }
        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public String resultString(int[] _firstGroup, int[] _secondGroup) {
        StringBuilder mySB = new StringBuilder();
        mySB.append("There are ").append(_firstGroup.length).append(" news in Group 1, and ").append(_secondGroup.length).append(" in Group 2.\r\n").append("=====Group 1=====\r\n");

        for (int i : _firstGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }
        mySB.append("=====Group 2=====\r\n");
        for (int i : _secondGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }

        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

}
