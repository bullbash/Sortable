package main;

import processors.*;
import ui.*;

public class Globals {
    public static int         mainFrameWidth    = 440;
    public static int         mainFrameHeight   = 300;
    public static MainFrame   mainFrame         = null;
    public static boolean     flagRun           = false;

    public static Worker      worker            = null;
    public static String      productsFileName  = "products.txt";
    public static String      listingsFileName  = "listings.txt";
//    public static String      resultsFileName   = "results.txt";
    public static String      resultsFileName   = "results_";
    
    public static Pattern[]   patterns          = null;
    public static int         dendriteSize          = 0;
    public static int         dendriteNumber        = 0;
    public static int         dendriteSpreads       = 0;

}
