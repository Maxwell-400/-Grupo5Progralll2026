package miumg.edu.gt.nodesheet;

import javax.swing.JFrame;
import javax.swing.UIManager;
import miumg.edu.gt.nodesheet.View.BibliotecaUI;
import miumg.edu.gt.nodesheet.View.SplashScreen;
import miumg.edu.gt.nodesheet.View.login;

public class NodeSheet {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame splashFrame = new JFrame();
        splashFrame.setSize(1300, 500);
        splashFrame.setLocationRelativeTo(null);

        SplashScreen splash = new SplashScreen();
        splashFrame.add(splash);

        splashFrame.setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashFrame.dispose();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(300, 300);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.add(new login());

                frame.setVisible(true);
            }
        });
    }
}
