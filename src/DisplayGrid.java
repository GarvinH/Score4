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
        GridToScreenRatio = maxY / ((world.length+1)*(world.length));  //ratio to fit in screen as square map

        System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);

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

            for(int i = 0; i<world[0][0].length;i=i+1)
            {
                for(int j = 0; j<world[0].length;j=j+1)
                {
                    for (int k = 0; k <world.length; k++) {
                        if (world[i][j][k] == 1)    //This block can be changed to match character-color pairs
                            g.setColor(Color.RED);
                        else if (world[i][j][k] == -1)
                            g.setColor(Color.YELLOW);
                        else
                            g.setColor(Color.BLUE);
                        g.fillRect(k * GridToScreenRatio + i*(world.length-1)*2*GridToScreenRatio+2, j * GridToScreenRatio-1, GridToScreenRatio, GridToScreenRatio);
                        g.setColor(Color.BLACK);
                        g.drawRect(k * GridToScreenRatio + i*(world.length-1)*2*GridToScreenRatio+2, j * GridToScreenRatio-1, GridToScreenRatio, GridToScreenRatio);
                    }
                }
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid
