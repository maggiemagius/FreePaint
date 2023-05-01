
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Stack;
import java.io.*;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener
{
	 private static final int MAX_UNDO = 255;
	    private static final Shape INITIAL_SHAPE = Shape.RECT;
	    private static final Tool INITIAL_TOOL = Tool.PENCIL;
	    private static final Color INITIAL_COLOR = Color.RED;
	    private static final Color COLOR_BACKGROUND = Color.WHITE;
	    private enum         State { IDLE, DRAGGING }

	    private JButton _undoButton;
	    private JButton _redoButton;
	    private PaintPreferences _currentPrefs;
	    private PaintAction _currentAction;
	    private Stack<PaintAction> _undoStack = new Stack<PaintAction>();
	    private Stack<PaintAction> _redoStack = new Stack<PaintAction>();
	    private State _state = State.IDLE;
	    private Tool  _tool  = INITIAL_TOOL;
	    private Shape _shape = INITIAL_SHAPE;
	    
	    private Point _start = null;
	    private Point _end   = null;
	    
	    //    BufferedImage stores the underlying saved painting.
	    //    Initialized first time paintComponent is called.
	    //TODO: use for saving and loading
	    private BufferedImage _bufImage = null;
	    
	    

	    public CanvasPanel() {
	    	
	        setBackground(Color.white);
	        
	        this.addMouseListener(this);
	        this.addMouseMotionListener(this);
	    }
	    
	    public void setUndoRedoButtons(JButton undo, JButton redo) {
	    	_undoButton = undo;
	    	_redoButton = redo;
	    	
	    	validateUndoRedoButtons();
	    }
	    
	    public void setPreferences(PaintPreferences prefs) {
	    	_currentPrefs = prefs;
	    }

	    public void setShape(Shape shape) {
	        _shape = shape;
	    }
	    
	    public void setTool(Tool tool) {
	        _tool = tool;
	    }
	    
	    private BufferedImage createBuffer() {
	    	int w = this.getWidth();
            int h = this.getHeight();
            BufferedImage img = (BufferedImage)this.createImage(w, h);
            Graphics2D gc = img.createGraphics();
            gc.setColor(COLOR_BACKGROUND);
            gc.fillRect(0, 0, w, h); 
            return img;
	    }

	    @Override public void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D)g;  
	        

	        if (_bufImage == null) {
	        	_bufImage = createBuffer();
	        }
	        
	        if (_undoStack.size() > MAX_UNDO) {
	        	PaintAction first = _undoStack.remove(0);
	        	first.render(_bufImage.createGraphics());
	        }

	        paintImage(g2);
	        
	    }
	    
	    private void paintImage(Graphics2D graphics) {
	    	graphics.drawImage(_bufImage, null, 0, 0);
	        
	        for (int i = 0; i < _undoStack.size(); i++) {
	        	PaintAction action = _undoStack.get(i);
	        	action.render(graphics);
	        }
	        
	        if (_currentAction != null) {
	        	_currentAction.render(graphics);
	        }
	    }

	    public void mousePressed(MouseEvent e) {
	        _state = State.DRAGGING;  
	        
	        _redoStack.clear();
	        validateUndoRedoButtons();
	        
//	        _start = e.getPoint();     
//	        _end   = _start;           
	        switch (_tool)
        	{
	        case PENCIL:
	        	_currentAction = new PencilAction(_currentPrefs);
	        	break;
	        case ERASER:
	        	_currentAction = new PencilAction(new PaintPreferences(_currentPrefs.thickness, Color.WHITE));
	        	break;
	        case SHAPE:
        		_currentAction = new ShapeAction(_shape, e.getPoint(), _currentPrefs);
        		break;
        	}
	    }
	    

	    public void mouseDragged(MouseEvent e) {
	        _state = State.DRAGGING;   
	        
//	        _end = e.getPoint();       
//	        this.repaint();
	        
	        if (_currentAction != null) {
	        	_currentAction.mouseDragged(e);
	        }
	        
	        this.repaint();
	    }
	    

	    public void mouseReleased(MouseEvent e) {

	    	
	        if (_currentAction != null) {
	        	_currentAction.mouseReleased(e);
//	        	_currentAction.render(_bufImage.createGraphics());
	        	this.repaint();
	        	
	        	_undoStack.push(_currentAction);
		    	
		    	validateUndoRedoButtons();
		    	
	        	_currentAction = null;
	        }
	    }
	    
	    public void undo() {
	    	if (_undoStack.size() <= 0) {
	    		return;
	    	}
	    	_redoStack.add(_undoStack.pop());
	    	
	    	validateUndoRedoButtons();
	    	
	    	this.repaint();
	    }
	    
	    public void redo() {
	    	if (_redoStack.size() <= 0) {
	    		return;
	    	}
	    	_undoStack.add(_redoStack.pop());
	    	
	    	validateUndoRedoButtons();
	    	
	    	this.repaint();
	    }
	    
	    public void save() {

			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			
			int returnValue = jfc.showSaveDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File outputFile = jfc.getSelectedFile();
				
				if (!outputFile.getAbsolutePath().endsWith(".png")) {
					outputFile = new File(outputFile.getParentFile(), outputFile.getName() + ".png");
				}
				
				BufferedImage toSave = createBuffer();
		    	paintImage(toSave.createGraphics());
		    	
		    	try {
		    		ImageIO.write(toSave, "png", outputFile);
		    		JOptionPane.showMessageDialog(null, "Image has been saved to: " + outputFile.getAbsolutePath(), "Success!", JOptionPane.INFORMATION_MESSAGE);
		    	}
		    	catch (Exception e) {
		    		JOptionPane.showMessageDialog(null, "Error saving image", "Error", JOptionPane.ERROR_MESSAGE);
		    	}
			}
	    }
	    
	    public void load() {

			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

			jfc.setAcceptAllFileFilterUsed(false);
	        jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
	        jfc.addChoosableFileFilter(new FileNameExtensionFilter("PNG file", "png"));
			
			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File inputFile = jfc.getSelectedFile();
		    	
		    	try {
		    		BufferedImage loaded = ImageIO.read(inputFile);
		    		
		    		_bufImage = createBuffer();
		    		Graphics2D graphics = _bufImage.createGraphics();
		    		graphics.drawImage(loaded, 0, 0, loaded.getWidth(), loaded.getHeight(), null);
		    		graphics.dispose();
		    		_undoStack.clear();
		    		_redoStack.clear();
		    		validateUndoRedoButtons();
		    		this.repaint();
		    	}
		    	catch (Exception e) {
		    		JOptionPane.showMessageDialog(null, "Error saving image", "Error", JOptionPane.ERROR_MESSAGE);
		    	}
			}
	    }
	    
	    private void validateUndoRedoButtons() {
	    	_redoButton.setEnabled(_redoStack.size() > 0);
	    	_undoButton.setEnabled(_undoStack.size() > 0);
	    }

	    public void mouseMoved  (MouseEvent e) {}
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited (MouseEvent e) {}
	    public void mouseClicked(MouseEvent e) {}
	
}
