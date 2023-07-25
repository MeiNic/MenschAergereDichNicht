import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Set the look and feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            new Landingpage();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        
        }
    }
}