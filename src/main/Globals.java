package main;

import processors.*;
import ui.*;

import java.awt.image.BufferedImage;

public class Globals {
    public static int           mainFrameWidth       = 500;
    public static int           mainFrameHeight      = 530;
    public static MainFrame     mainFrame            = null;
    public static boolean       flagRun              = false;

    public static Worker        worker               = null;
    public static String        productsFileName     = "products.txt";
    public static String        listingsFileName     = "listings.txt";
//    public static String      resultsFileName   = "results.txt";
    public static String        resultsFileName      = "results_";
    
    public static Pattern[]     patterns             = null;
    public static int           dendriteSize         = 0;
    public static int           dendriteNumber       = 0;
    public static int           dendriteSpreads      = 0;
    public static int           outputListingsNumber = 0;
    
    public static BufferedImage distributionImage    = null;
    public static int           imagePanelHeight     = 200;
    public static ImagePanel    imagePanel           = new ImagePanel();
    

}
