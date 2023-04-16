
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener
{
	//TODO: add resizing of the canvas
	 private static final int SIZE = 300;  
	    private static final Shape INITIAL_SHAPE = Shape.RECT;
	    private static final Tool INITIAL_TOOL = Tool.PENCIL;
	    private static final Color INITIAL_COLOR = Color.RED;
	    private static final Color COLOR_BACKGROUND = Color.WHITE;
	    private enum         State { IDLE, DRAGGING }

	    

	    private State _state = State.IDLE;
	    private Tool  _tool  = INITIAL_TOOL;
	    private Shape _shape = INITIAL_SHAPE;
	    private Color _color = INITIAL_COLOR;
	    
	    private Point _start = null;
	    private Point _end   = null;
	    
	    //    BufferedImage stores the underlying saved painting.
	    //    Initialized first time paintComponent is called.
	    //TODO: use for saving and loading
	    private BufferedImage _bufImage = null;
	    
	    

	    public CanvasPanel() {
	        setPreferredSize(new Dimension(SIZE, SIZE));
	        setBackground(Color.white);
	        
	        this.addMouseListener(this);
	        this.addMouseMotionListener(this);
	    }
	    

	    public void setShape(Shape shape) {
	        _shape = shape;
	    }
	    
	    public void setTool(Tool tool) {
	        _tool = tool;
	    }
	    

	    public void setColor(Color color) {
	        _color = color;
	    }
	    

	    @Override public void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D)g;  
	        

	        if (_bufImage == null) {

	            int w = this.getWidth();
	            int h = this.getHeight();
	            _bufImage = (BufferedImage)this.createImage(w, h);
	            Graphics2D gc = _bufImage.createGraphics();
	            gc.setColor(COLOR_BACKGROUND);
	            gc.fillRect(0, 0, w, h); 
	        }
	        

	        g2.drawImage(_bufImage, null, 0, 0);
	        

	        if (_state == State.DRAGGING) {
	        	switch (_tool)
	        	{
		        	case PENCIL:
		        		drawPencilLine(_bufImage.createGraphics());
		        		this.repaint();
		        		break;
		        	case SHAPE:
		        		drawCurrentShape(g2); //The creates a ghost image of the shape while it is dragging.
		        		break;
		        	default:
		        		break;
	        	}
	        }
	    }
	    
		private void drawPencilLine(Graphics2D g2) {
			g2.setColor(_color);
			g2.setPaintMode();
			//TODO: Line Thickness settings
			g2.drawLine(_start.x, _start.y, _end.x, _end.y);
			_start = _end;
		}
		
	    private void drawCurrentShape(Graphics2D g2) 
	    {
	       //TODO: needs to draw shape 
	        
	        
	    }
	    

	    public void mousePressed(MouseEvent e) {
	        _state = State.DRAGGING;  
	        
	        _start = e.getPoint();     
	        _end   = _start;           
	    }
	    

	    public void mouseDragged(MouseEvent e) {
	        _state = State.DRAGGING;   
	        
	        _end = e.getPoint();       
	        this.repaint();           
	    }
	    

	    public void mouseReleased(MouseEvent e) {

	        _end = e.getPoint();      
	        if (_state == State.DRAGGING) {
	            _state = State.IDLE;
	        	switch (_tool)
	        	{
		        	case SHAPE:
		        		drawCurrentShape(_bufImage.createGraphics());
		        		break;
		        	default:
		        		break;
	        	}
	            
	            this.repaint();
	        }
	    }
	    

	    public void mouseMoved  (MouseEvent e) {}
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited (MouseEvent e) {}
	    public void mouseClicked(MouseEvent e) {}
	
}
