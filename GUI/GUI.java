import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI implements ActionListener{
    
    //Global GUI variable
    static GUI GUI = new GUI();
    //Global Frame for GUI
    static JFrame frame = new JFrame("3-D Chess Game");


    public JMenuBar createMenuBar() {
    	//Create variables used for the Menu Bar
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the MenuBar
        menuBar = new JMenuBar();

        //Build the File Menu
        menu = new JMenu("File");
        menuBar.add(menu);

        // Creating the File Menu
        
        //Menu Item for New Game, can be selected by Mouse Click, or by CTRL + N
        menuItem = new JMenuItem("New Game",
                                 KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        //Adding Action Listener for New Game 
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Help Page, can be selected by Mouse Click, or by CTRL + H
        menuItem = new JMenuItem("Help", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.CTRL_MASK));
      //Adding Action Listener for Help
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Settings Page, can be selected by Mouse Click, or by CTRL + S
        menuItem = new JMenuItem("Settings", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
      //Adding Action Listener for Settings
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Exit, can be selected by Mouse Click, or by Escape
        menuItem = new JMenuItem("Exit", KeyEvent.VK_ESCAPE);
        menuItem.setMnemonic(KeyEvent.VK_ESCAPE);
      //Adding Action Listener for Exit
        menuItem.addActionListener(this);
        menu.add(menuItem);
 
        return menuBar;
        
        
    }

    public Container createContentPane() {
        //Create the content pane to be used in the future
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);      
        return contentPane;
    }

    public JOptionPane createSettingsJOptionPane(){
    //Creating the JOptionPane for Settings
    JPanel panel = new JPanel();
    panel.add(new JRadioButton("Human vs. Computer"));
    panel.add(new JRadioButton("Computer vs. Computer"));
    panel.add(new JRadioButton("Computer vs. Human"));
    panel.add(new JRadioButton("Human vs. Human"));
    JOptionPane pane = new JOptionPane();
    JOptionPane.showOptionDialog(null, panel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    return pane;
    }
    
    public JOptionPane createHelpJOptionPane(){
    //Creating the JOptionPane for Help
    JPanel panel = new JPanel();
    panel.add(new JRadioButton("GoodLuck!"));
    JOptionPane pane = new JOptionPane();
    JOptionPane.showOptionDialog(null, panel, "Help", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    return pane;
    }
    
    

   //Creating and showing the GUI
    
    public static void createAndShowGUI() {
        //Create and set up the window.
      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
     
        frame.setJMenuBar(GUI.createMenuBar());
        
        //Setting the Background Image
        try{
    		frame.setContentPane(new JLabel(new ImageIcon (ImageIO.read(new File("chess.png")))));
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}

        //Display the window.
        //frame.setSize(800, 550);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        
        //Frame is not re-sizeable
        frame.setResizable(false);
        frame.setVisible(true);
        //Centering the window
        frame.setLocationRelativeTo(null);
     
    }
    
public void actionPerformed(ActionEvent e){
	
		if (e.getActionCommand().equals("New Game")){
		//
		}

	
		if (e.getActionCommand().equals("Help")){
		frame.setContentPane(GUI.createHelpJOptionPane());
		}

	
		if (e.getActionCommand().equals("Settings")){
			frame.setContentPane(GUI.createSettingsJOptionPane());
		}
		
		
		if (e.getActionCommand().equals("Exit")){
			System.exit(0);
		}
    	
    }
    
    
  
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //Creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}