package scoliosis;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import static scoliosis.Main.*;
import static scoliosis.mainjframe.mainframe;


public class Game {

    static int xcoord = 1;
    static int ycoord = 4;

    static int appleX = 7;
    static int appleY = 4;
    static int length = 0;


    static int direction = 3;

    static String board = "";

    static double leftovertime = 0;

    static int fpslockmax = 150;
    static double timelast = System.currentTimeMillis();

    static int boardSize = 10;

    static int[] prevXcoords = new int[0];
    static int[] prevYcoords = new int[0];

    static boolean died = false;

    static String boardChar = " ";

    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {
        mainframe.requestFocus();

        if (bs != null) {

            if (KeyLib.keyPressed(KeyEvent.VK_W)) direction = 0;
            else if (KeyLib.keyPressed(KeyEvent.VK_A)) direction = 1;
            else if (KeyLib.keyPressed(KeyEvent.VK_S)) direction = 2;
            else if (KeyLib.keyPressed(KeyEvent.VK_D)) direction = 3;

            if (xcoord == appleX && ycoord == appleY) {
                length++;
                newAppleCords();
                int[] savedX = prevXcoords;
                int[] savedY = prevYcoords;

                prevXcoords = new int[length];
                prevYcoords = new int[length];

                for (int i = 0; i < savedX.length; i++) {
                    prevXcoords[i] = savedX[i];
                    prevYcoords[i] = savedY[i];
                }

                prevXcoords[length-1] = -1;
                prevYcoords[length-1] = -1;
            }

            board = "";
            for (int p = 0; p <= boardSize*boardSize; p++) {
                boolean found = false;
                for (int i = 0; i < prevXcoords.length; i++) {
                    if (p == (10*prevYcoords[i]) + prevXcoords[i]) found = true;
                }

                if (p == (10*ycoord) + xcoord) board += "O ";
                else if (p == (10*appleY) + appleX) board += "a ";
                else if (found) board += "o ";
                else board += boardChar + " ";
            }

            for (int i = 0; i < prevXcoords.length; i++) {
                if ((prevXcoords[i] == xcoord && prevYcoords[i] == ycoord)) died = true;
            }
            if (xcoord >= 9 || ycoord >= 9 || xcoord < 0 || ycoord < 0) died = true;




            String[] board2 = new String[boardSize];
            for (int s = 0; s < (boardSize+1)*2; s+=2) {
                if (board.length() > (s * boardSize) + (boardSize*2)) {
                    board2[s/2] = board.substring(s * boardSize, (s * boardSize) + (boardSize*2));
                }
            }

            leftovertime = (System.currentTimeMillis() - timelast) - fpslockmax;

            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            Font font = new Font("Comic Sans MS", 1, (int) (15 / 480f * (float) (mainframe.getWidth())));
            g.setFont(font);
            g.setColor(new Color(0, 0, 0));


            int xco = 0;
            int yco = 0;


            for (int i = 0; i < board2.length; i++) {
                g.drawString(board2[i], 10, 15 * i + 20);
                if (10+g.getFontMetrics(font).stringWidth(board2[i]) > xco) xco = 10+g.getFontMetrics(font).stringWidth(board2[i]);
                if (i == board2.length-1) yco = 15 * i + 20 + g.getFontMetrics(font).getDescent();
            }

            g.drawRect(8, 10, xco-8, yco-10);

            if (died) {
                g.drawString("you die", 200, 100);
            }

            g.dispose();
            bs.show();

            if (!died) {
                while (leftovertime >= fpslockmax) {
                    leftovertime -= fpslockmax;

                    timelast = System.currentTimeMillis();

                    if (length > 0) {

                        for (int i = length - 1; i > 0; i--) {
                            prevXcoords[i] = prevXcoords[i - 1];
                            prevYcoords[i] = prevYcoords[i - 1];
                        }
                        prevXcoords[0] = xcoord;
                        prevYcoords[0] = ycoord;

                    }

                    if (direction == 0) ycoord--;
                    else if (direction == 1) xcoord--;
                    else if (direction == 2) ycoord++;
                    else if (direction == 3) xcoord++;
                }
            }
        }
    }

    public static void newAppleCords() {
        appleX = (int) (Math.random() * boardSize);
        appleY = (int) (Math.random() * boardSize);

        for (int i = 0; i < prevXcoords.length; i++) {
            if ((prevXcoords[i] == appleX && prevYcoords[i] == appleY) || (xcoord == appleX && ycoord == appleY)) newAppleCords();
        }

    }

}
