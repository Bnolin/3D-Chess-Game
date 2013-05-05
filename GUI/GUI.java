import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
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
        //Menu Item for Chess Notation Page, can be selected by Mouse Click, or by CTRL + C
        menuItem = new JMenuItem("Chess Notation", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Chess Notation
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for How To Play Page, can be selected by Mouse Click, or by CTRL + H
        menuItem = new JMenuItem("How To Play", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        //Adding Action Listener for How to Play
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Settings Page, can be selected by Mouse Click, or by CTRL + S
        menuItem = new JMenuItem("Settings", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Settings
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Full Screen, can be selected by Mouse Click, or by CTRL + F
        menuItem = new JMenuItem("Full Screen", KeyEvent.VK_F);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Full Screen
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Menu Item for Exit, can be selected by Mouse Click, or by CTRL + E
        menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.CTRL_MASK));
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

    ButtonGroup Players = new ButtonGroup();
    ButtonGroup Difficulty = new ButtonGroup();
    
    public JOptionPane createSettingsJOptionPane(){
    //Creating the JOptionPane for Settings
    JPanel panel = new JPanel();
    //Creating a label for Player Options
    JLabel players = new JLabel("Player vs. Player:");
    //Creating a label for Difficulty Options
    JLabel difficulty = new JLabel("Difficulty:");
    //Creating a spacer to put between groups of buttons
    JLabel spacer = new JLabel("                     ");
   
    //Creating All Radio Buttons for Players and add then to Players ButtonGroup
    JRadioButton HumanVSComputer = new JRadioButton("Human vs. Computer");
    Players.add(HumanVSComputer);
    JRadioButton ComputerVSHuman = new JRadioButton("Computer vs. Human");
    Players.add(ComputerVSHuman);
    JRadioButton ComputerVSComputer = new JRadioButton("Computer vs. Computer");
    Players.add(ComputerVSComputer);
    JRadioButton HumanVSHuman = new JRadioButton("Human vs. Human");
    Players.add(HumanVSHuman);
    
    //Creating All Radio Buttons for Difficulty
    JRadioButton Easy = new JRadioButton("Easy");
    Difficulty.add(Easy);
    JRadioButton Medium = new JRadioButton("Medium");
    Difficulty.add(Medium);
    JRadioButton Hard = new JRadioButton("Hard");
    Difficulty.add(Hard);
    
    //Adding Buttons to panel
    //Add Players Buttons to panel
    panel.add(players);
    panel.add(HumanVSComputer);
    panel.add(ComputerVSHuman);
    panel.add(ComputerVSComputer);
    panel.add(HumanVSHuman);
    //spacer
    panel.add(spacer,"span, grow");
    //Add Difficulty Buttons to panel
    panel.add(difficulty);
    panel.add(Easy);
    panel.add(Medium);
    panel.add(Hard);
   
    JOptionPane pane = new JOptionPane();
    JOptionPane.showOptionDialog(frame, panel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    return pane;
    }
    
    public JOptionPane createChessNotationJOptionPane(){
    	//Creating the JOptionPane for Chess Notation
        JOptionPane pane = new JOptionPane();
        JTextArea textArea = new JTextArea();
        
        try {
        	   String line;
        	   FileReader fileReader = new FileReader("ChessNotation.txt");
        	   BufferedReader bufferedReader = new BufferedReader(fileReader);
        	 
        	   while((line = bufferedReader.readLine())!= null){
        	     textArea.append(line + "\n");
        	   } 
        	   bufferedReader.close();
        	}
        	   catch (IOException e) {
        	   System.err.println(e);
        	   System.exit(1);
        	}
        
        textArea.setFont(new Font("Serif",Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800,800));
        JOptionPane.showMessageDialog(null, scrollPane , "Chess Notation", JOptionPane.INFORMATION_MESSAGE, null);
        return pane;
        }
    
    public JOptionPane createHowToPlayJOptionPane(){
    //Creating the JOptionPane for Help
    JOptionPane pane = new JOptionPane();
    JTextArea textArea = new JTextArea();
    
    try {
    	   String line;
    	   FileReader fileReader = new FileReader("HowToPlay.txt");
    	   BufferedReader bufferedReader = new BufferedReader(fileReader);
    	 
    	   while((line = bufferedReader.readLine())!= null){
    	     textArea.append(line + "\n");
    	   } 
    	   bufferedReader.close();
    	}
    	   catch (IOException e) {
    	   System.err.println(e);
    	   System.exit(1);
    	}
    
    textArea.setFont(new Font("Serif",Font.PLAIN, 16));
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setCaretPosition(0);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(800,800));
    JOptionPane.showMessageDialog(null, scrollPane , "How To Play", JOptionPane.INFORMATION_MESSAGE, null);
    return pane;
    }
    
    

   //Creating and showing the GUI
    
    public static void createAndShowGUI() {
        //Create and set up the window.
      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
     
        frame.setJMenuBar(GUI.createMenuBar());
        
        Color bgcolor = new Color(1,1,1);
        frame.getContentPane().setBackground(bgcolor);
        
        //Setting the Background Image
        try{
    		frame.getContentPane().add(new JLabel(new ImageIcon (ImageIO.read(new File("background.png")))));
    	
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
        
    
        
        //Display the window.
        frame.setSize(800, 800);
        //Frame is not re-sizable
        frame.setResizable(false);
        frame.setVisible(true);
        //Centering the window
        frame.setLocationRelativeTo(null);
     
    }
    
//Variables for storing the settings

    
@SuppressWarnings("deprecation")
public void actionPerformed(ActionEvent e){
	
		if (e.getActionCommand().equals("New Game")){
			/*Game g = new Game(new ComputerPlayer(5), new ComputerPlayer(5));
			Display d = new Display(g);
			Container frame1 = d.getContentPane();
			frame.setContentPane(frame1);
			g.playGame();*/
			
		}

		if (e.getActionCommand().equals("Chess Notation")){
			frame.setContentPane(GUI.createChessNotationJOptionPane());

		}
	
		if (e.getActionCommand().equals("How To Play")){
			frame.setContentPane(GUI.createHowToPlayJOptionPane());

		}

	
		if (e.getActionCommand().equals("Settings")){
			frame.setContentPane(GUI.createSettingsJOptionPane());
		}
		
		if (e.getActionCommand().equals("Full Screen")){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			int width = screenSize.width;
			int height = screenSize.height;
			frame.resize(width, height);
			frame.setLocationRelativeTo(null);
			//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
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