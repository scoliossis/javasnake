package scoliosis;

import scoliosis.Libs.KeyLib;

import javax.swing.*;
import java.awt.*;

import static scoliosis.Libs.ScreenLib.screenSize;

public class mainjframe extends JFrame {
    public static JFrame mainframe = new JFrame();


    public static void DrawDisplay() {
        Display game = new Display();

        mainframe = new JFrame("scoliosis - snake");
        mainframe.add(game);

        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize((int) screenSize.getWidth() / 4, (int) screenSize.getHeight() / 4);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        mainframe.addKeyListener(new KeyLib());

        game.startGame();
    }
}
