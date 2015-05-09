package main;

import ui.MainFrame;

public class Main {

    public static void main(String[] args) {
        System.out.println("Sortable Challenge v.01 started.");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Globals.mainFrame = new MainFrame();
                Globals.mainFrame.setVisible(true);
            }
        });
    }

}
