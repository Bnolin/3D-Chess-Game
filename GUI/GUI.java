package GUI;

import Backend.*;
import Display.*;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;


public class GUI implements ActionListener{
    
    private static final int EXIT_ON_CLOSE = 0;
	//Global GUI variable
    static GUI GUI = new GUI();
    //Global Frame for GUI
    static JFrame frame = new JFrame("3-D Chess Game");

    private static JMenu menu;
    private static JMenuItem menuExitItem;
    private static JMenuItem menuFullScreenItem;
    private static JMenuItem menuExitFullScreenItem;
    
    public JMenuBar createMenuBar() {
    	//Create variables used for the Menu Bar
        JMenuBar menuBar;
        
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
        menuFullScreenItem = new JMenuItem("Full Screen", KeyEvent.VK_F);
        menuFullScreenItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Full Screen
        menuFullScreenItem.addActionListener(this);
        menu.add(menuFullScreenItem);
        //Menu Item for exiting Full Screen mode, can be selected by Mouse Click or CTRL + Z
        menuExitFullScreenItem = new JMenuItem ("Exit Full Screen", KeyEvent.VK_Z);
        menuExitFullScreenItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menuExitFullScreenItem.addActionListener(this);
        //Menu Item for Exiting Game, can be selected by Mouse Click, or by CTRL + Escape
        menuExitItem = new JMenuItem("Exit Game", KeyEvent.VK_ESCAPE);
        menuExitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Exit
        menuExitItem.addActionListener(this);
        //Menu Item for Exit, can be selected by Mouse Click, or by CTRL + E
        menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        //Adding Action Listener for Exit
        menuItem.addActionListener(this);
        menu.add(menuItem);
        //Adding Action Listener to Menu
        menu.addActionListener(this);
        menu.setActionCommand("Menu");
        
        return menuBar;
        
        
    }

    public Container createContentPane() {
        //Create the content pane to be used in the future
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);      
        return contentPane;
    }

    //Global Variable for the Player Selection in Settings Menu
    private int PlayerSelection = 0;
    //Global Variable for the Difficulty Selection in Settings Menu
    private int DifficultySelection = 0;
    //For use in creating a New Game, Default set to Human vs. Computer
	private static Player Player1 = new HumanPlayer();
	//For use in creating a New Game, Default set to Easy Difficulty
	private static Player Player2 = new ComputerPlayer(3);
    
    public JOptionPane createSettingsJOptionPane(){
    //Creating the JOptionPane for Settings
    JPanel panel = new JPanel();
    //Creating a label for Player Options
    JLabel players = new JLabel("Player vs. Player:");
    //Creating the options for the Players ComboBox   
    String playerList[] = {"Human vs. Computer", "Computer vs. Human", "Computer vs. Computer", "Human vs. Human"};
    //Creating the players ComboBox
    JComboBox playersCB = new JComboBox(playerList);
    //Setting starting index for players ComboBox
    playersCB.setSelectedIndex(0);
    //Add Player Options Label and ComboBox
    panel.add(players);
    panel.add(playersCB);
    JLabel difficulty = new JLabel("Difficulty:");
    //Creating the options for the difficulty ComboBox   
    String difficultyList[] = {"Easy", "Medium", "Hard"};
    //Creating the difficulty ComboBox
    JComboBox difficultyCB = new JComboBox(difficultyList);
    //Setting starting index for difficulty ComboBox
    difficultyCB.setSelectedIndex(0);
    panel.add(difficulty);
    panel.add(difficultyCB);
   
    JOptionPane pane = new JOptionPane();
    int result = JOptionPane.showOptionDialog(panel, panel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE , null, null, null);
    if (result == JOptionPane.OK_OPTION){
		int Player = playersCB.getSelectedIndex();
		int Difficulty = difficultyCB.getSelectedIndex();
		PlayerSelection = Player;
		DifficultySelection = Difficulty;
		
			switch(PlayerSelection){
			case 0: 
				Player1 = new HumanPlayer();
				Player2 = new ComputerPlayer(DifficultySelection + 3);
				break;
			case 1:
				Player1 = new ComputerPlayer(DifficultySelection + 3);
				Player2 = new HumanPlayer();
				break;
			case 2:
				Player1 = new ComputerPlayer(DifficultySelection + 3);
				Player2 = new ComputerPlayer(DifficultySelection + 3);
				break;
			case 3:
				Player1 = new HumanPlayer();
				Player2 = new HumanPlayer();
				break;
			}		
	}
    pane.removeAll();
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
        scrollPane.setPreferredSize(new Dimension(700,600));
        JOptionPane.showMessageDialog(pane, scrollPane , "Chess Notation", JOptionPane.INFORMATION_MESSAGE, null);
        pane.removeAll();
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
    scrollPane.setPreferredSize(new Dimension(700,700));
    JOptionPane.showMessageDialog(frame, scrollPane , "How To Play", JOptionPane.INFORMATION_MESSAGE, null);
    pane.removeAll();
    return pane;
    }
    
    
    private static JLabel background;
    private static JButton newGameButton;
    private static JButton chessNotationButton;
    private static JButton howToPlayButton;
    private static JButton settingsButton;
    private static JButton fullScreenButton;
    private static JButton exitFullScreenButton;

   //Creating and showing the GUI
    
    static MP3 menump3;
    static MP3 gamemp3;
    public static void createAndShowGUI() {
    	
    	menump3 = new MP3("menu.mp3");
    	menump3.play();
    	
        //Create and set up the window.
      
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
     
        frame.setJMenuBar(GUI.createMenuBar());
        
        Color bgcolor = new Color(1,1,1);
        frame.getContentPane().setBackground(bgcolor);
        
        //frame.setLayout(new BorderLayout());
    	background = new JLabel(new ImageIcon("background.png"));
    	frame.add(background);
    	//background.setLayout(new FlowLayout());
    	
    	newGameButton = new JButton("New Game");
     	background.add(newGameButton);
     	newGameButton.setSize(120,30);
     	newGameButton.setLocation(340,200);
        newGameButton.addActionListener( new ActionListener() {
             @Override
             public void actionPerformed( ActionEvent aActionEvent ) {
            		Game g = new Game(Player1, Player2);
        			Display d = new Display(g);
        			frame.remove(background);
        			frame.getContentPane().add(d.canvas);
        			d.canvas.requestFocus();
        			d.canvas.setVisible(true);
        			frame.setSize(850,800);
        			g.t.t.start();
        		    menu.add(menuExitItem);
        			menump3.close();
        			gamemp3 = new MP3("Beethoven.mp3");
        			gamemp3.play();
             }
           } );
    	
    	chessNotationButton = new JButton("Chess Notation");
    	background.add(chessNotationButton);
    	chessNotationButton.setSize(120,30);
    	chessNotationButton.setLocation(340,270);
        chessNotationButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
            frame.setContentPane(GUI.createChessNotationJOptionPane());
            }
          } );
        
       
        howToPlayButton = new JButton("How To Play");
    	background.add(howToPlayButton);
    	howToPlayButton.setSize(120,30);
    	howToPlayButton.setLocation(340,340);
    	howToPlayButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
             frame.setContentPane(GUI.createHowToPlayJOptionPane());
            }
          } );
    	
    	
        settingsButton = new JButton("Settings");
    	background.add(settingsButton);
    	settingsButton.setSize(120,30);
    	settingsButton.setLocation(340,410);
    	settingsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent aActionEvent ) {
            frame.setContentPane(GUI.createSettingsJOptionPane());
            }
          } );
    	
    	exitFullScreenButton = new JButton("Exit Full Screen");
       	exitFullScreenButton.addActionListener( new ActionListener() {
               @Override
               public void actionPerformed( ActionEvent aActionEvent ) {
            	   //Display the window.
                   frame.setSize(800, 800);
                   //Frame is not re-sizable
                   frame.setResizable(false);
                   frame.setVisible(true);
                   //Centering the window
                   frame.setLocationRelativeTo(null);
                   menuExitFullScreenItem.setVisible(false);
               }
             } );
    	
    	fullScreenButton = new JButton("Full Screen");
       	background.add(fullScreenButton);
       	fullScreenButton.setSize(120,30);
       	fullScreenButton.setLocation(340,480);
       	fullScreenButton.addActionListener( new ActionListener() {
               @Override
               public void actionPerformed( ActionEvent aActionEvent ) {
       			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
    			/*frame.resize(width, height);
    			frame.setLocationRelativeTo(null);*/
    			background.remove(fullScreenButton);
    			background.add(exitFullScreenButton);
    			menu.add(menuExitFullScreenItem);
    			exitFullScreenButton.setEnabled(true);
    			exitFullScreenButton.setVisible(true);
    			menuFullScreenItem.setVisible(false);
    			int height = (screenSize.height - 800)/2;
    			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    			newGameButton.setLocation((screenSize.width/2-60), height + 200);
    			chessNotationButton.setLocation((screenSize.width/2-60), height + 270);
    			howToPlayButton.setLocation((screenSize.width/2-60), height + 340);
    			settingsButton.setLocation((screenSize.width/2-60), height + 410);
    			exitFullScreenButton.setLocation((screenSize.width/2-60), height + 480);
               }
             } );
       	
    
        
    	
        //Display the window.
        frame.setSize(800, 800);
        //Frame is not re-sizable
        frame.setResizable(false);
        frame.setVisible(true);
        //Centering the window
        frame.setLocationRelativeTo(null);
     
    }
    

    
public void actionPerformed(ActionEvent e){
		if (e.getActionCommand().equals("Menu")){
			menu.requestFocus();
		}
		if (e.getActionCommand().equals("New Game")){
			
			Game g = new Game(Player1, Player2);
			Display d = new Display(g);
			frame.remove(background);
			frame.getContentPane().add(d.canvas);
			d.canvas.requestFocus();
			d.canvas.setVisible(true);
			frame.setSize(850,800);
			g.t.t.start();
		    menu.add(menuExitItem);
			menump3.close();
			gamemp3 = new MP3("Beethoven.mp3");
			gamemp3.play();
			

		}
		if (e.getActionCommand().equals("Exit Game")){
			frame.setContentPane(background);
			frame.setBackground(new Color (1,1,1));
			background.requestFocus();
			background.setVisible(true);
			frame.setSize(800,800);
			newGameButton.setVisible(true);
			menu.remove(menuExitItem);
			gamemp3.close();
			menump3.play();
			

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
			background.remove(fullScreenButton);
			background.add(exitFullScreenButton);
			menu.add(menuExitFullScreenItem);
			exitFullScreenButton.setEnabled(true);
			exitFullScreenButton.setVisible(true);
			menuFullScreenItem.setVisible(false);
			int height = (screenSize.height - 800)/2;
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			newGameButton.setLocation((screenSize.width/2-60), height + 200);
			chessNotationButton.setLocation((screenSize.width/2-60), height + 270);
			howToPlayButton.setLocation((screenSize.width/2-60), height + 340);
			settingsButton.setLocation((screenSize.width/2-60), height + 410);
			exitFullScreenButton.setLocation((screenSize.width/2-60), height + 480);			
		}
    	
		if (e.getActionCommand().equals("Exit Full Screen")){
			  //Display the window.
	        frame.setSize(800, 800);
	        //Frame is not re-sizable
	        frame.setResizable(false);
	        frame.setVisible(true);
	        //Centering the window
	        frame.setLocationRelativeTo(null);
	        menuExitFullScreenItem.setVisible(false);
	        menuFullScreenItem.setVisible(true);
	        fullScreenButton.setVisible(true);
			
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