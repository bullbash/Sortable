package processors;

import main.*;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Worker extends Thread{
    ArrayList<String> productsNames      = null;
    ArrayList<String> listingsNames      = null;
    ArrayList<String> sourceListings     = null;
    
    ArrayList<int[]>  digitizedProducts  = null;
    ArrayList<int[]>  digitizedListings  = null;
    PrintWriter       out                = null;
    JProgressBar      progressBar        = null;       
    JLabel            productLabel       = null;
    JLabel            etaLabel           = null;
    long              startTime          = 0;
    
    JButton           startButton        = null;
    JButton           stopButton         = null;
    
    boolean           flagStop           = false;
    int[]             metricDistribution = null;
    
                        // ================== constructor ======================
    public Worker(JButton sButton, JButton stButton, JLabel pLabel, JProgressBar pBar, JLabel eLabel){
        startButton       = sButton;
        stopButton        = stButton;
        productLabel      = pLabel;
        progressBar       = pBar;
        etaLabel          = eLabel;
        productsNames     = new ArrayList();
        listingsNames     = new ArrayList();
        digitizedProducts = new ArrayList();
        digitizedListings = new ArrayList();
        sourceListings    = new ArrayList();
        System.out.println("Max comparable substring length is : "+Globals.patterns[0].size);
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(Globals.resultsFileName+"_"+Globals.dendriteSize+""+Globals.dendriteNumber+""+Globals.dendriteSpreads+"_"+Globals.patterns[0].size+".txt", false)));
            out.println("// dendrite Size = "+Globals.dendriteSize+", dendrites Number = "+Globals.dendriteNumber+", dendrites Spread = "+Globals.dendriteSpreads);
            out.println("// Max Neuron Size = "+Globals.patterns[0].size+", Neuron Types = "+Globals.patterns.length);
//            out.println("Listing line format : \"similarity metric\":int , \"original index in listings file\":int , \"title\":String");
        }
        catch(Exception e){
            System.out.println("Worker.constructor : "+e);
        }
        setPriority(MAX_PRIORITY);
    }
                        // ====================== run ==========================
    public void run(){
        flagStop = false;
        System.out.println("Worker Thread started");
        productsDigitizer();
        listingsDigitizer();
        progressBar.setMaximum(digitizedListings.size());
        startButton.setVisible(false);
        stopButton.setVisible(true);
        Globals.distributionImage = null;
        Globals.imagePanel.repaint();
        for(int i = 0; i < digitizedProducts.size(); i++){
            startTime = System.currentTimeMillis();
            TreeMap<Integer, Integer>    sortedView         = new TreeMap<Integer, Integer>(Collections.reverseOrder());

            productLabel.setText("#"+i+" : "+productsNames.get(i));

            int  maxMetric          = 0;
                 metricDistribution = new int[Globals.patterns.length];
            
            for(int j = 0; j < digitizedListings.size(); j++){
                if(j > 0 && j % 10 == 0){
                    progressBar.setValue(j);
                    progressBar.setString("Listing " + j+"/"+digitizedListings.size());
                    long elapsed = (System.currentTimeMillis() - startTime)/1000;
                    int  elapMin = (int)(elapsed/60);
                    int  elapSec = (int)(elapsed - elapMin * 60);
                    long toGo    = (digitizedListings.size() - j) * elapsed / j;
                    int  toGoMin = (int)(toGo / 60);
                    int  toGoSec = (int)(toGo - toGoMin * 60);
                    etaLabel.setText("Elapsed "+elapMin+":"+elapSec+", ETA in "+toGoMin+":"+toGoSec);
                }
                
                int metric = getMetric(i, j) * 100;     // by multiplying by 100, we make a busket size 100 for similar metric units
                                                        // with no control yet over busket overfilling
                while(sortedView.containsKey(metric)){  // that is a hack, better key control will make it slower
                    metric--;
                }
                sortedView.put(metric, j);              // sorted metrics container to investigate cut off parameters
                if(flagStop) break;
            }
            if(flagStop) break;
            printProduct(i);
            
            prepareShowDistributionImage(metricDistribution );
            
            int outputCounter = 0;
            for(Integer metric : sortedView.keySet()) {
                int   listingIndex = sortedView.get(metric);
                printListing(metric, listingIndex);
                if(outputCounter++ > Globals.outputListingsNumber) break;
                out.println(",");
            }
            out.println("\n]}");
            out.flush();
        }
        out.close();
        startButton.setVisible(true);
        stopButton.setVisible(false);
    }
                        // ================ productsDigitizer ==================
    public void productsDigitizer(){
        FileInputStream fis = null;
        try{
                            fis           = new FileInputStream(Globals.productsFileName);
            BufferedReader  br            = new BufferedReader(new InputStreamReader(fis));
            String          line          = null;
            while ((line = br.readLine()) != null){
                stripProduct(line, digitizedProducts, productsNames);
            }
            fis.close();
            fis = null;
        }
        catch(Exception e){
            System.out.println("Worker.productDigitizer : "+e);
        }
        if(fis != null){
            try{fis.close();} catch(Exception e){}
        }
    }
                        // ================ listingsDigitizer ==================
    public void listingsDigitizer(){
        FileInputStream fis = null;
        try{
                            fis           = new FileInputStream(Globals.listingsFileName);
            BufferedReader  br            = new BufferedReader(new InputStreamReader(fis));
            String          line          = null;
            while ((line = br.readLine()) != null){
                sourceListings.add(line);
                stripProduct(line, digitizedListings, listingsNames);
            }
            fis.close();
            fis = null;
        }
        catch(Exception e){
            System.out.println("Worker.listingsDigitizer : "+e);
        }
        if(fis != null){
            try{fis.close();} catch(Exception e){}
        }
    }
                        // ================ stripProduct =======================
    void stripProduct(String line, ArrayList digitizedList, ArrayList namesList){
        int index = line.indexOf(":") + 2;
            line  = line.substring(index);
            index = line.indexOf(",") - 1;
            line  = line.substring(0, index);
            namesList.add(line);
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < line.length(); i++){
                char ch = line.charAt(i);
                if(ch != ' ' && ch != '-' && ch != '_')
                    sb.append(ch);
            }
            int[] digitizedArray = new int[sb.length()];
            for(int i = 0; i < sb.length(); i++){
                digitizedArray [i] = sb.charAt(i);
            }
            digitizedList.add(digitizedArray);
    }
                        // ================== getMetric ========================
    int getMetric(int ii, int jj){
        int[] product = (int[])digitizedProducts.get(ii);
        int[] listing = (int[])digitizedListings.get(jj);
        int metric = 0;
        for(int i = 0; i < Globals.patterns.length; i++){
            int[] pattern = Globals.patterns[i].pattern;
            
            for(int j = 0; j < product.length; j++){
                int[] productPattern = new int[pattern.length];
                int productSum = 0;
                if(j + pattern.length <= product.length){
                    for(int k = 0; k < pattern.length; k++){
                        productPattern[k] = product[j + k] & pattern[k];
                        productSum += productPattern[k];
                    }
                }
                if(productSum > 0){
                    for(int l = 0; l < listing.length; l++){
                        int[] listingPattern = new int[pattern.length];
                        int listingSum = 0;
                        if(l + pattern.length <= listing.length){
                            for(int k = 0; k < pattern.length; k++){
                                listingPattern[k] = listing[l + k] & pattern[k];
                                listingSum += listingPattern[k];
                            }
                        }
                        if(listingSum > 0){
                            // here we have productPattern and listingPattern, they must be identical
                            boolean flagIdentical = true;
                            for(int k = 0; k < pattern.length; k++){
                                if(productPattern[k] != listingPattern[k]){
                                    flagIdentical = false;
                                    break;
                                }
                            }
                            if(flagIdentical){
                                metric += 1;            // could be just +1, we weight it by the length of identical pattern
//                                metric += pattern.length - l;            // could be just +1, we weight it by the length of identical pattern
//                                metric += pattern.length - l - l;            // could be just +1, we weight it by the length of identical pattern
//                                metric += (pattern.length << 1) - l;            // could be just +1, we weight it by the length of identical pattern
                            }
                        }
                    }
//        metric = metric / listing.length;
                }
            }
            metricDistribution[i] += metric;         // to investigate value of neurons of different sizes, metric is a similarity measure
        }
//        metric = metric / listing.length;
        return metric;
    }
                            // ============= printProduct ======================
    void printProduct(int ii){
        String productName = productsNames.get(ii);
//        out.println("\n"+productName);
        out.println("{\"product_name\":"+productName+",\"listings\":[");
        
    }
                            // ============= printListing ======================
    void printListing(int metric, int ii){
        String listingIndex = String.format("%6d", ii);
//        out.println("    \""+metric+"\", \""+listingIndex+"\", \""+listingsNames.get(ii)+"\"");
        out.print("    "+sourceListings.get(ii));
    }
                            // ============== stopWorker =======================
    public void stopWorker(){
        flagStop = true;
    }
    void prepareShowDistributionImage(int[] distributionArray){
        int maxMetric = 0;
        for(int i = 0; i < distributionArray.length; i++){
            if(distributionArray[i] > maxMetric) maxMetric = distributionArray[i];
        }
        if(maxMetric <= 0) return;  // there were no identical substrings
        Globals.distributionImage = new BufferedImage(distributionArray.length, Globals.imagePanelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = Globals.distributionImage.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, distributionArray.length, maxMetric);
        g.setColor(Color.blue);
        for(int i = 0; i < distributionArray.length; i++){
            int lineHeight = distributionArray[i] * Globals.imagePanelHeight / maxMetric;  
            g.drawLine(i, Globals.imagePanelHeight, i, Globals.imagePanelHeight - lineHeight);
        }
        g.dispose();
        Globals.imagePanel.repaint();
    }
}
