package kwic.es;

/**
 * Created by xuawai on 21/04/2018.
 */
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;

public class WordsIndex implements Observer {


    public WordsIndex(Map wordsIndexMapOriginal, Map wordsIndexMap){
        this.wordsIndexMapOriginal = wordsIndexMapOriginal;
        this.wordsIndexMap = wordsIndexMap;
    }

    private Map<String, Integer> wordsIndexMapOriginal;
    private Map<String, Integer> wordsIndexMap;

    public void update(Observable observable, Object arg){
        LineStorageWrapper lines = (LineStorageWrapper) observable;
        LineStorageChangeEvent event = (LineStorageChangeEvent) arg;

        switch(event.getType()){
        case LineStorageChangeEvent.ADD:
            String[] line = lines.getLine(lines.getLineCount() - 1);
            //original index
            for(int k = 0; k < line.length; k++) {
                String word = line[k];
                if (wordsIndexMapOriginal.containsKey(word)) {
                    int index = wordsIndexMapOriginal.get(word);
                    wordsIndexMapOriginal.put(word, ++index);
                } else {
                    wordsIndexMapOriginal.put(word, 1);
                }
            }

            //shifter index
            for(int i = 0; i < line.length; i++){
                ArrayList words = new ArrayList();
                for(int j = i; j < (line.length + i); j++)
                    words.add(line[j % line.length]);
                for(int k = 0; k < words.size(); k++) {
                    String word = (String) words.get(k);
                    if (wordsIndexMap.containsKey(word)) {
                        int index = wordsIndexMap.get(word);
                        wordsIndexMap.put(word, ++index);
                    }else{
                        wordsIndexMap.put(word, 1);
                    }
                }

            }
            break;

        case LineStorageChangeEvent.DELETE:
            String name = event.getArg();
            String[] nameArr = name.split("\\s+");
            // original index
            for (int k = 0; k < nameArr.length; k++) {
                String word = nameArr[k];
                int index = wordsIndexMapOriginal.get(word);
                if(index!=1)
                    wordsIndexMapOriginal.put(word, --index);
                else
                    wordsIndexMapOriginal.remove(word);   //为0时删除
            }

            // shifter index
            for(int i = 0; i < nameArr.length; i++) {
                ArrayList words = new ArrayList();
                for (int j = i; j < (nameArr.length + i); j++)
                    words.add(nameArr[j % nameArr.length]);
                for (int k = 0; k < words.size(); k++) {
                    String word = (String) words.get(k);
                    int index = wordsIndexMap.get(word);
                    if(index!=1)
                        wordsIndexMap.put(word, --index);
                    else
                        wordsIndexMap.remove(word);   //为0时删除
                }
            }
            break;

        default:
            break;
        }
    }
}
