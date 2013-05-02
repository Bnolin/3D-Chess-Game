package Display;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
	import java.nio.FloatBuffer;
	import java.nio.IntBuffer;

	import javax.media.opengl.*;
	import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
	import javax.swing.JFrame;
	import javax.vecmath.Point3f;
	import javax.vecmath.Vector3f;

import Backend.*;

	import com.sun.opengl.util.BufferUtil;
	import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Display extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, Observer{

	Game g;
	ChessBoard b;
	Move lastMove;
	String textInput = "";
	float translated = 0;
	
	class objModel {
			public FloatBuffer vertexBuffer;
			public IntBuffer faceBuffer;
			public FloatBuffer normalBuffer;
			public Point3f center;
			public float bottom;
			public int num_verts;		// number of vertices
			public int num_faces;		// number of triangle faces

			public void Draw() {
				vertexBuffer.rewind();
				normalBuffer.rewind();
				faceBuffer.rewind();
				gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
				
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
				gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);
				
				gl.glDrawElements(GL.GL_TRIANGLES, num_faces*3, GL.GL_UNSIGNED_INT, faceBuffer);
				
				gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
			}
			
			public objModel(String filename) {
				/* load a triangular mesh model from a .obj file */
				BufferedReader in = null;
				try {
					in = new BufferedReader(new FileReader(filename));
				} catch (IOException e) {
					System.out.println("Error reading from file " + filename);
					System.exit(0);
				}

				center = new Point3f();	
				float x, y, z;
				int v1, v2, v3;
				float minx, miny, minz;
				float maxx, maxy, maxz;
				float bbx, bby, bbz;
				minx = miny = minz = 10000.f;
				maxx = maxy = maxz = -10000.f;
				
				String line;
				String[] tokens;
				ArrayList<Point3f> input_verts = new ArrayList<Point3f> ();
				ArrayList<Integer> input_faces = new ArrayList<Integer> ();
				ArrayList<Vector3f> input_norms = new ArrayList<Vector3f> ();
				try {
				while ((line = in.readLine()) != null) {
					if (line.length() == 0)
						continue;
					switch(line.charAt(0)) {
					case 'v':
						tokens = line.split("[ ]+");
						x = Float.valueOf(tokens[1]);
						y = Float.valueOf(tokens[2]);
						z = Float.valueOf(tokens[3]);
						minx = Math.min(minx, x);
						miny = Math.min(miny, y);
						minz = Math.min(minz, z);
						maxx = Math.max(maxx, x);
						maxy = Math.max(maxy, y);
						maxz = Math.max(maxz, z);
						input_verts.add(new Point3f(x, y, z));
						center.add(new Point3f(x, y, z));
						break;
					case 'f':
						tokens = line.split("[ ]+");
						v1 = Integer.valueOf(tokens[1])-1;
						v2 = Integer.valueOf(tokens[2])-1;
						v3 = Integer.valueOf(tokens[3])-1;
						input_faces.add(v1);
						input_faces.add(v2);
						input_faces.add(v3);				
						break;
					default:
						continue;
					}
				}
				in.close();	
				} catch(IOException e) {
					System.out.println("Unhandled error while reading input file.");
				}

				System.out.println("Read " + input_verts.size() +
							   	" vertices and " + input_faces.size() + " faces.");
				
				center.scale(1.f / (float) input_verts.size());
				
				bbx = maxx - minx;
				bby = maxy - miny;
				bbz = maxz - minz;
				float bbmax = Math.max(bbx, Math.max(bby, bbz));
				
				for (Point3f p : input_verts) {
					
					p.x = (p.x - center.x) / bbmax;
					p.y = (p.y - center.y) / bbmax;
					p.z = (p.z - center.z) / bbmax;
				}
				bottom = (miny - center.y) / bbmax;
					center.x = center.y = center.z = 0.f;
				
				/* estimate per vertex average normal */
				int i;
				for (i = 0; i < input_verts.size(); i ++) {
					input_norms.add(new Vector3f());
				}
				
				Vector3f e1 = new Vector3f();
				Vector3f e2 = new Vector3f();
				Vector3f tn = new Vector3f();
				for (i = 0; i < input_faces.size(); i += 3) {
					v1 = input_faces.get(i+0);
					v2 = input_faces.get(i+1);
					v3 = input_faces.get(i+2);
					
					e1.sub(input_verts.get(v2), input_verts.get(v1));
					e2.sub(input_verts.get(v3), input_verts.get(v1));
					tn.cross(e1, e2);
					input_norms.get(v1).add(tn);
					
					e1.sub(input_verts.get(v3), input_verts.get(v2));
					e2.sub(input_verts.get(v1), input_verts.get(v2));
					tn.cross(e1, e2);
					input_norms.get(v2).add(tn);
					
					e1.sub(input_verts.get(v1), input_verts.get(v3));
					e2.sub(input_verts.get(v2), input_verts.get(v3));
					tn.cross(e1, e2);
					input_norms.get(v3).add(tn);			
				}

				/* convert to buffers to improve display speed */
				for (i = 0; i < input_verts.size(); i ++) {
					input_norms.get(i).normalize();
				}
				
				vertexBuffer = BufferUtil.newFloatBuffer(input_verts.size()*3);
				normalBuffer = BufferUtil.newFloatBuffer(input_verts.size()*3);
				faceBuffer = BufferUtil.newIntBuffer(input_faces.size());
				
				for (i = 0; i < input_verts.size(); i ++) {
					vertexBuffer.put(input_verts.get(i).x);
					vertexBuffer.put(input_verts.get(i).y);
					vertexBuffer.put(input_verts.get(i).z);
					normalBuffer.put(input_norms.get(i).x);
					normalBuffer.put(input_norms.get(i).y);
					normalBuffer.put(input_norms.get(i).z);			
				}
				
				for (i = 0; i < input_faces.size(); i ++) {
					faceBuffer.put(input_faces.get(i));	
				}			
				num_verts = input_verts.size();
				num_faces = input_faces.size()/3;
			}		
		}
		
		/* GL, display, model transformation, and mouse control variables */
		private final GLCanvas canvas;
		private GL gl;
		private final GLU glu = new GLU();	
		private FPSAnimator animator;
		private TextRenderer text;

		private int winW = 800, winH = 800;
		private boolean wireframe = false;
		private boolean flatshade = false;
		
		private float xpos = 0, ypos = 0, zpos = 0;
		private float centerx, centery, centerz;
		private float znear, zfar;
	
		private objModel pawn = new objModel("pawn.obj");
		private objModel rook = new objModel("rook.obj");
		private objModel knight = new objModel("knight.obj");
		private objModel bishop = new objModel("bishop.obj");
		private objModel queen = new objModel("queen.obj");
		private objModel king = new objModel("king.obj");

		/* Here you should give a conservative estimate of the scene's bounding box
		 * so that the initViewParameters function can calculate proper
		 * transformation parameters to display the initial scene.
		 * If these are not set correctly, the objects may disappear on start.
		 */
		private float xmin = -4f, ymin = 0f, zmin = -5f;
		private float xmax = 4f, ymax = 0f, zmax = 5f;
		private Texture boardTexture;	
		public float looking = -.25f;
		
		public synchronized void display(GLAutoDrawable drawable) {
			if(animator.isAnimating() && (lastMove.equals(g.currentMove) || g.currentMove == null)){				
				animator.stop();				
			}
			if(!animator.isAnimating() && !lastMove.equals(g.currentMove) && g.currentMove != null){
				animator.start();
			}
			boolean moving = false;
			if(!lastMove.equals(b.lastMove)){
				translated ++;
				moving = true;
			}
			if(translated == 30){
				translated = 0;
				lastMove = b.lastMove;
				b = g.b;
				moving = false;
				looking = -looking;
				horozontalChange = 0;
				heightChange = 0;
			}			
			
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, wireframe ? GL.GL_LINE : GL.GL_FILL);	
			gl.glShadeModel(flatshade ? GL.GL_FLAT : GL.GL_SMOOTH);		
			gl.glEnable(GL.GL_CULL_FACE);		
			
			gl.glLoadIdentity();
			
				text.beginRendering(800, 800);
				text.draw(textInput, 0, winH);
				text.draw("Time used by White: " + g.toTime(g.player1Time), 0, 30);
				text.draw("Time used by Black: " + g.toTime(g.player2Time), 0, 0);
				text.endRendering();
				
			/* this is the transformation of the entire scene */
			gl.glTranslatef(-xpos, -ypos, -zpos);
			gl.glTranslatef(centerx, centery, centerz);	
			
			
	        glu.gluLookAt(horozontalChange, .144+heightChange, looking, 0, 0, 0, 0, 1, 0);
	        gl.glNormal3f(0,0,looking);

						
			gl.glTranslatef(-centerx, -centery, -centerz);
			
			Piece p;
						
			float[] rgba = {1f, 1f, 1f};
		    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
		    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);
			
		    boardTexture.enable();
			boardTexture.bind();
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(-4, 0, -4);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(-4, 0, 4);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(4, 0, 4);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(4, 0, -4);
			gl.glEnd();
			gl.glFlush();
			boardTexture.disable();
			
			
			float transI = (b.lastMove.From().Col()-b.lastMove.To().Col())*(1-translated/30.f);
			float transJ = (b.lastMove.From().Row()-b.lastMove.To().Row())*(1-translated/30.f);
						
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					p = b.pieceAt(i,j);
					if(p != Piece.empty){
						gl.glPushMatrix();
						
						if(p.color() == Color.White){
						    float mat_ambient[] = { 0, 0, 0, 1 };
						    float mat_specular[] = { 1, 1, 1, 1 };
						    float mat_diffuse[] = { 1, 1, 1, 1 };
						    float mat_shininess[] = { 128 };
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
						} else {
						    float mat_ambient[] = { 0f, 0f, 0f, 1 };
						    float mat_specular[] = { 1, 1, 1, 1 };
						    float mat_diffuse[] = { .3f, .3f, .3f, 0 };
						    float mat_shininess[] = { 128 };
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
						    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
						}
						gl.glTranslatef(-.5f, 0, .5f);
						if(i != b.lastMove.To().Col() || j != b.lastMove.To().Row() || !moving){
							gl.glTranslatef(4-i, 0, j-4);
						} else {
							gl.glTranslatef(4-(i+transI), 0, (j+transJ)-4);
						}
						switch(p){
						case blackPawn:case whitePawn:
							gl.glScalef(.75f, .75f, .75f);
							gl.glTranslatef(0, -pawn.bottom, 0);pawn.Draw();
							break;
						case blackRook:case whiteRook:
							gl.glTranslatef(0, -rook.bottom, 0);rook.Draw();
							break;
						case blackKnight:gl.glRotatef(180, 0, 1, 0);
						case whiteKnight:
							gl.glTranslatef(0, -knight.bottom, 0);
							gl.glRotatef(-45,0,1,0);
							knight.Draw();break;
						case blackBishop:case whiteBishop:gl.glTranslatef(0, -bishop.bottom, 0);bishop.Draw();break;
						case blackQueen:case whiteQueen:gl.glTranslatef(0, -queen.bottom, 0);queen.Draw();break;
						case blackKing:case whiteKing:gl.glTranslatef(0, -king.bottom, 0);king.Draw();break;
						}
						gl.glPopMatrix();
					}
				}
			}
		}	
		
		public Display(Game g) {
			super("3D-Chess-Game");
			this.g = g;
			b = g.b;
			lastMove = g.b.lastMove;
			canvas = new GLCanvas();
			canvas.addGLEventListener(this);
			canvas.addKeyListener(this);
			canvas.addMouseListener(this);
			canvas.addMouseMotionListener(this);
			g.addObserver(this);
			animator = new FPSAnimator(canvas, 30);	// create a 30 fps animator
			text = new TextRenderer(new Font("SansSerif", Font.BOLD, 18));
			getContentPane().add(canvas);
			setSize(winW, winH);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			animator.start();
			canvas.requestFocus();
		}
		public void init(GLAutoDrawable drawable) {
			gl = drawable.getGL();

			initViewParameters();
			gl.glClearColor(.3f, .3f, .3f, 1f);
			gl.glClearDepth(1.0f);

		    // white light at the eye
//			float light0_position[] = { 0, 0, 1, 0 };
//	    	float light0_diffuse[] = { 1, 1, 1, 1 };
//	    	float light0_specular[] = { 1, 1, 1, 1 };
//	    	gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, light0_position, 0);
//		    gl.glLightfv( GL.GL_LIGHT0, GL.GL_DIFFUSE, light0_diffuse, 0);
//		    gl.glLightfv( GL.GL_LIGHT0, GL.GL_SPECULAR, light0_specular, 0);
			
	        float[] lightPos = {-30, 0, 0, 1};
	        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
	        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

	        // Set light parameters.
	        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
	        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
	        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);
			
		    //material

		    float lmodel_ambient[] = { 0, 0, 0, 1 };
		    gl.glLightModelfv( GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
		    gl.glLightModeli( GL.GL_LIGHT_MODEL_TWO_SIDE, 1 );

		    gl.glEnable( GL.GL_NORMALIZE );
		    gl.glEnable( GL.GL_LIGHTING );
		    gl.glEnable( GL.GL_LIGHT0 );
		    gl.glEnable( GL.GL_LIGHT1 );
		    gl.glEnable( GL.GL_LIGHT2 );
	
		    
		    gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LESS);
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
			gl.glCullFace(GL.GL_BACK);
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glShadeModel(GL.GL_SMOOTH);
			
			try {
	            TextureData data = TextureIO.newTextureData(new File("board.png"), false, TextureIO.PNG);
	            boardTexture = TextureIO.newTexture(data);
	        }
	        catch (IOException exc) {
	            exc.printStackTrace();
	            System.exit(1);
	        }
		}
		public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
			winW = width;
			winH = height;

			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL.GL_PROJECTION);
				gl.glLoadIdentity();
				glu.gluPerspective(45.f, (float)width/(float)height, znear, zfar);
			gl.glMatrixMode(GL.GL_MODELVIEW);
		}
				
		/* computes optimal transformation parameters for OpenGL rendering.
		 * this is based on an estimate of the scene's bounding box
		 */	
		void initViewParameters()
		{
			float ball_r = (float) Math.sqrt((xmax-xmin)*(xmax-xmin)
								+ (ymax-ymin)*(ymax-ymin)
								+ (zmax-zmin)*(zmax-zmin)) * 0.707f;

			centerx = (xmax+xmin)/2.f;
			centery = (ymax+ymin)/2.f;
			centerz = (zmax+zmin)/2.f;
			xpos = centerx;
			ypos = centery;
			zpos = ball_r/(float) Math.sin(45.f*Math.PI/180.f)+centerz;

			znear = 0.01f;
			zfar  = 1000.f;
			

		}	
		String validChars = "RNBQK0abcdefgh12345678x-";
		private int mouseX;
		private int mouseY;
		private int mouseButton;
		private float heightChange;
		private float horozontalChange;
		public void keyPressed(KeyEvent e) {
			if((g.turn == Color.White && g.player1.getClass() == HumanPlayer.class) || (g.turn == Color.Black && g.player2.getClass() == HumanPlayer.class)){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(g.turn == Color.White && g.player1.getClass() == HumanPlayer.class){
						HumanPlayer tempP = (HumanPlayer) g.player1;
						tempP.input(textInput);
						textInput = "";
					}
					if(g.turn == Color.Black && g.player2.getClass() == HumanPlayer.class){
						HumanPlayer tempP = (HumanPlayer) g.player2;
						tempP.input(textInput);
						textInput = "";
					}
				} else 
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					if(textInput.length() > 0){
						textInput = textInput.substring(0,textInput.length()-1);
					}
				}else
				if(validChars.contains(""+e.getKeyChar())){textInput += ""+e.getKeyChar();}
				
				if(e.getKeyChar() == 'r'){heightChange = 0;horozontalChange = 0;}
				
			}
			if(!animator.isAnimating()){
				animator.start();
			}
		}

		
		
		
		// these event functions are not used for this assignment
		public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			if(!animator.isAnimating()){
				animator.start();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (mouseButton == MouseEvent.BUTTON1) {
				horozontalChange -= (x - mouseX)/100.f;
				horozontalChange = Math.max(horozontalChange, -1);
				horozontalChange = Math.min(horozontalChange, 1);
				
				heightChange += (y - mouseY)/100.f;
				heightChange = Math.max(heightChange,0);
				heightChange = Math.min(heightChange,1);
				mouseX = x;
				mouseY = y;
				canvas.display();
			}
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			mouseButton = e.getButton();
			canvas.display();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseButton = MouseEvent.NOBUTTON;
			canvas.display();
		}

	}

