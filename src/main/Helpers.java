package main;

import processors.Pattern;

import java.util.*;

public class Helpers {
                    // =================== getCombos ===========================
//    public static String[] getCombos(int fragmentSize, int fragmentsNumber, int gapSize) {
    public static Pattern[] getCombos(int fragmentSize, int fragmentsNumber, int gapSize) {
        int[][] sizes = getCombinations(fragmentSize, fragmentsNumber);
        int fragSizesCombinations = sizes.length;
        int[][] gaps  = getCombinations(gapSize     , fragmentsNumber - 1);
        int gapsSizesCombinations = gaps.length;
        
        ArrayList st = new ArrayList();
        ArrayList le = new ArrayList();
        HashMap keys = new HashMap();
        for(int i = 1; i < fragSizesCombinations; i++){                         // starting without zero length substring like (0, 0, 0, 0)
            int[] fragsSize = sizes[i];
            int[] starts    = new int[fragsSize.length];
            int[] lengths   = new int[fragsSize.length];
            for(int j = 0; j < gapsSizesCombinations; j++){
                int[] gapsSizes = gaps[j];
                int start  = 0;
                String key = "";
                for(int l = 0; l < fragsSize.length; l++){
                    starts [l] = start;
                    lengths[l] = fragsSize[l];
                    if(fragsSize[l] > 0){
                        if(!(l > 0 && key.isEmpty())){
                            key +="("+start+","+lengths[l]+") ";
                        }
                    }
                    start += fragsSize[l];
                    if(l < gapsSizes.length){
                        start += gapsSizes [l];
                    }
                }
                if(!keys.containsKey(key) && !key.isEmpty()){
                    keys.put(key, key);
                    st.add(starts.clone());
                    le.add(lengths.clone());
                }
            }
        }
        int iii = 0;
//-----------------------------------------        
        int numberOfCombinations = st.size();
        int[][] s = new int[numberOfCombinations][fragmentsNumber];
        int[][] l = new int[numberOfCombinations][fragmentsNumber];
        int[][] g = new int[numberOfCombinations][fragmentsNumber - 1];         // gaps if any
        int[]   c = new int[numberOfCombinations];                              // combined length of a combination
        
        ArrayList<String> combos = new ArrayList<>();
        for(int i = 0; i < st.size(); i++){
            int[] starts    = (int[])st.get(i);
            int[] lengths   = (int[])le.get(i);
            s[i] = starts;
            l[i] = lengths;
            int ll = 0;
            for(int j = 0; j < fragmentsNumber - 1; j++){
                ll += l[i][j];
                if(l[i][j] > 0){
                    for(int m = j+1; m < fragmentsNumber; m++){
                        if(l[i][m] > 0){
                            int gap = s[i][m] - s[i][j] - l[i][j];
                            if(gap > 0){
                                ll += gap;
                                g[i][j] = gap;
                            }
                            else{
                                g[i][j] = 0;
                            }
                            break;
                        }
                    }
                }
            }
            ll += l[i][fragmentsNumber - 1];
            c[i] = ll;
            String str = "";            
            for(int j = 0; j < fragmentsNumber ; j++){
                for(int t = 0; t < l[i][j]; t ++){
                    str += "A";
                }
                if(j < fragmentsNumber - 1){
                    for(int t = 0; t < g[i][j]; t ++){
                        str += "*";
                    }
                }
            }            
            if(str.length() > 1 && !combos.contains(str)){
                combos.add(str);
            }
        }
        TreeMap<String, String>    sortedView    = new TreeMap();
        int combosNumber = combos.size();
        for(int i = 0; i < combosNumber; i++)        {
            sortedView.put(combos.get(i), null);    // 
        }

        String[]  array = new String [combosNumber];
        Pattern[] coms  = new Pattern[combosNumber];
        int counter = 0;
        for(String key : sortedView.keySet()) {
            array[counter] = key;
            Pattern pattern = new Pattern(key.length());
            for(int i = 0; i < key.length(); i++){
                if(key.charAt(i) == 'A') pattern.pattern[i] = Integer.MAX_VALUE;
                else                     pattern.pattern[i] = 0;
            }
            coms[counter] = pattern;
            counter++;
        }

        Pattern[] newComs = new Pattern[combosNumber];
        
        int[] combosSizes   = new int[combosNumber];
        int[] combosIndexes = new int[combosNumber];
        for(int i = 0; i < coms.length; i++){                                   // rearange - long combinations - first!
            combosSizes   [i] = coms[i].size;
            combosIndexes [i] = i;
        }
        
        for(int i = 0; i < coms.length; i++){                                   
            int size1 = combosSizes [i];
            for(int j = i+1; j < coms.length; j++){
                int size2 = combosSizes [j];
                if(size2 > size1){
                    int cs = combosSizes [i];
                    combosSizes   [i] = combosSizes [j];
                    combosSizes   [j] = cs;
                    size1             = size2;
                    int iiii = combosIndexes [i];
                    combosIndexes [i] = combosIndexes [j];
                    combosIndexes [j] = iiii;
                }
            }
        }
        for(int i = 0; i < coms.length; i++){                                   
            int ind = combosIndexes [i];
            newComs [i] = coms[ind];
        }
        return newComs;
    }
                        // =================== getCombinations =================
                        // wordLength = fragment(gap) size, wordsNumber - frags(gaps) number
    private static int[][] getCombinations (int wordLength, int wordsNumber){
        int longDimention = 1;
        for(int i = 0; i < wordsNumber; i++){
            longDimention *= wordLength;
        }
        int[][] sizes = new int[longDimention][wordsNumber];
        int step = 0;
        
        int[] dividers = new int[wordsNumber];
        int[] values   = new int[wordsNumber];
        int power = 1;
        for(int j = 0; j < wordsNumber; j++){
            dividers [j] = power;
            power = power * wordLength;
        }
        for(int i = 0; i < longDimention; i++){
            int num = step;
            for(int k = wordsNumber; k > 0; k--){
                values [k -1] = num / dividers [k -1];
                num = num - values [k -1] * dividers [k -1];
                sizes [i][k - 1] = values [k -1];
            }
            step++;
        }
        return sizes;
    }

}
