import javax.swing.*;
import java.awt.*;

public class LayoutTest extends JFrame {

    public LayoutTest(){
        JFrame frame = new JFrame("Super Awesome Window Title!"); //Create the JFrame and give it a title
        frame.setSize(1000, 200); //512 x 256px size
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Quit the application when the JFrame is closed

        JPanel pane = new JPanel(new GridBagLayout()); //Create a pane to house all content, and give it a GridBagLayout
        frame.setContentPane(pane);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel headerLabel = new JLabel("My Amazing Swing Application");
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 0;
        pane.add(headerLabel, c);

        JButton buttonA = new JButton("Button A");
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        pane.add(buttonA, c);

        JButton buttonB = new JButton("Button B");
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 1;
        pane.add(buttonB, c);

        JButton buttonC = new JButton("Button C");
        c.gridx = 2;
        c.gridwidth = 1;
        c.gridy = 1;
        pane.add(buttonC, c);

        JSlider slider = new JSlider(0, 100);
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 2;
        pane.add(slider, c);

        JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 20, 0, 100);
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 3;
        pane.add(scrollBar, c);

        frame.setVisible(true); //Show the window
    }

    public static void main(String[] args){
        new LayoutTest();
    }
}
