/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;


class DisplayGrid {

    private JFrame frame;
    private int maxX,maxY, GridToScreenRatio;
    private int[][][] world;

    DisplayGrid(int[][][] w) {
        this.world = w;

        maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
        GridToScreenRatio = maxY / ((world.length+2)*((int)Math.sqrt(world.length)));  //ratio to fit in screen as square map

        System.out.println("Map size: "+world.length+" by "+world[0].length + " by " + world[0][0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);

        this.frame = new JFrame("Map of World");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }


    public void refresh() {
        frame.repaint();
    }



    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {
            //super.repaint();

            setDoubleBuffered(true);
            g.setColor(Color.BLACK);
            int counter = 0;
            int row = 0;
            int rowY;

            for (int i = 0; i < world[0][0].length; i = i + 1) {
                for (int j = 0; j < world[0].length; j = j + 1) {
                    for (int k = 0; k < world.length; k++) {
                        rowY = j * GridToScreenRatio + GridToScreenRatio;
                        if (world[i][j][k] == 1)    //This block can be changed to match character-color pairs
                            g.setColor(Color.RED);
                        else if (world[i][j][k] == -1)
                            g.setColor(Color.YELLOW);
                        else
                            g.setColor(Color.BLUE);
                        if (i < world.length / 2) {
                            g.fillRect(k * GridToScreenRatio + i * (world.length - 1) * 2 * GridToScreenRatio + 2, rowY, GridToScreenRatio, GridToScreenRatio);
                            g.setColor(Color.BLACK);
                            g.drawRect(k * GridToScreenRatio + i * (world.length - 1) * 2 * GridToScreenRatio + 2, j * GridToScreenRatio + GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                            g.drawString("Level " + (i + 1), 2 * GridToScreenRatio + i * (world.length - 1) * 2 * GridToScreenRatio + 2, GridToScreenRatio / 2);
                        } else {
                            g.fillRect(k * GridToScreenRatio + (i - world.length / 2) * (world.length - 1) * 2 * GridToScreenRatio + 2, j * GridToScreenRatio + (world.length+1) * GridToScreenRatio + GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                            g.setColor(Color.BLACK);
                            g.drawRect(k * GridToScreenRatio + (i - world.length / 2) * (world.length - 1) * 2 * GridToScreenRatio + 2, j * GridToScreenRatio + (world.length+1) * GridToScreenRatio + GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                        }
                        if (counter > ((int)Math.sqrt(world.length))) {
                            counter = 0;
                            row += 1;
                        }
                    }
                }
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid