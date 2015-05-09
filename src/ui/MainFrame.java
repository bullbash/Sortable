package ui;

import main.*;
import processors.Worker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends javax.swing.JFrame {
    
    JButton      startButton  = new JButton("Start");
    JButton      stopButton   = new JButton("Stop/Dump Results");
    
    JProgressBar progressBar          = null;        
    JLabel       productLabel         = null;
    JLabel       etaLabel             = null;
    
    JLabel       outputSizeLabel      = new JLabel("Listings for each product in output");
    String[]     outputSizeModel      = {"50","100","500","1000","2000", "ALL"};
    JComboBox    outputSizes          = new JComboBox(outputSizeModel);
    
    JLabel       dendriteSizesLabel   = new JLabel("Dendrite Size :");
    String[]     dendriteSizesModel   = {"3","4","5","6"};
    JComboBox    dendriteSizes        = new JComboBox(dendriteSizesModel); 
    
    JLabel       dendriteNumberLabel  = new JLabel("Dendrites Number :");
    String[]     dendriteNumberModel  = {"2","3","4","5","6"};
    JComboBox    dendriteNumbers      = new JComboBox(dendriteNumberModel); 
    
    JLabel       dendritesSpreadLabel = new JLabel("Dendrites Spread :");
    String[]     dendriteSpreadsModel = {"2","3","4","5","6"};
    JComboBox    dendritesSpreads     = new JComboBox(dendriteSpreadsModel); 
    
    JLabel       neuronSizeLabel      = new JLabel("Whole Neuron Size : ");
    
    JLabel       imageTitleLabel      = new JLabel("Metric Increments Distribution per Neuron Size ");
    
                // ================== constructor ==============================
    public MainFrame(){
        setLayout(null);
        setBounds(0, 0, Globals.mainFrameWidth, Globals.mainFrameHeight);
        setTitle ("Sortable Challenge v.01");
        startButton.setBounds(10, 10, 100, 20);
        getContentPane().add(startButton);
        startButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)  {start();}  // property name, textField to show it
        });
        
        stopButton.setBounds(10, 10, 200, 20);
        stopButton.setVisible(false);
        getContentPane().add(stopButton);
        stopButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)  {Globals.worker.stopWorker();}  // property name, textField to show it
        });
        
        productLabel = new JLabel("Product");
        productLabel.setBounds(10, 40, 300, 20);
        getContentPane().add(productLabel);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBounds(10, 70, 240, 20);
        getContentPane().add(progressBar);
        
        etaLabel = new JLabel("ETA");
        etaLabel.setBounds(260, 70, 200, 20);
        getContentPane().add(etaLabel);
                
        dendriteSizesLabel.setBounds( 10, 120, 150, 20); getContentPane().add(dendriteSizesLabel);
        dendriteSizes     .setBounds(170, 120,  80, 20); getContentPane().add(dendriteSizes);
        dendriteSizes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent _ev){rebuildNeurons(); }
        });
        
        outputSizeLabel.setBounds(280, 120, 200, 20);  getContentPane().add(outputSizeLabel);
        outputSizes    .setBounds(320, 150, 100, 20);  getContentPane().add(outputSizes);
        outputSizes    .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent _ev){rebuildNeurons(); }
        });
        
        dendriteNumberLabel.setBounds( 10, 150, 150, 20); getContentPane().add(dendriteNumberLabel);
        dendriteNumbers    .setBounds(170, 150,  80, 20); getContentPane().add(dendriteNumbers);
        dendriteNumbers.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent _ev){rebuildNeurons(); }
        });
        
        dendritesSpreadLabel.setBounds( 10, 180, 150, 20); getContentPane().add(dendritesSpreadLabel);
        dendritesSpreads    .setBounds(170, 180,  80, 20); getContentPane().add(dendritesSpreads);
        dendritesSpreads.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent _ev){rebuildNeurons(); }
        });
        
        neuronSizeLabel.setBounds(10, 210, 400, 20); getContentPane().add(neuronSizeLabel);
        
        Globals.imagePanel.setBounds(10, 250, 400, Globals.imagePanelHeight);
        Globals.imagePanel.setBackground(Color.black);
        getContentPane().add(Globals.imagePanel);

        imageTitleLabel.setBounds(20, 460, 300, 20); getContentPane().add(imageTitleLabel);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Globals.flagRun = false;
                setVisible(false);
                dispose();
                System.exit(0);                   
            }
        });
        rebuildNeurons();
    }
                    // ================== start ================================
    void start(){
        Globals.worker = new Worker(startButton, stopButton, productLabel, progressBar, etaLabel);
        Globals.worker.start();
    }
                    // ================ rebuildNeurons =========================
    void rebuildNeurons(){
        setCursor(Cursor.WAIT_CURSOR);
        Globals.dendriteSize    = Integer.parseInt((String)dendriteSizes.getSelectedItem());
        Globals.dendriteNumber  = Integer.parseInt((String)dendriteNumbers.getSelectedItem());
        Globals.dendriteSpreads = Integer.parseInt((String)dendritesSpreads.getSelectedItem());
        Globals.patterns = Helpers.getCombos(Globals.dendriteSize, Globals.dendriteNumber, Globals.dendriteSpreads);
        neuronSizeLabel.setText("Max Neuron Size : "+Globals.patterns[0].pattern.length+
                                ", Min Neuron Size : "+Globals.patterns[Globals.patterns.length - 1].pattern.length+
                                ", Neuron Types : "+Globals.patterns.length);
        String outputString = (String)outputSizes.getSelectedItem();
        if(outputString.equals("ALL")) Globals.outputListingsNumber = Integer.MAX_VALUE;
        else{
            Globals.outputListingsNumber = Integer.parseInt(outputString);
        }
        
        setCursor(Cursor.DEFAULT_CURSOR);
    }
}
