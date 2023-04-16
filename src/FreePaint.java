import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;



public class FreePaint extends JPanel implements MouseListener, MouseMotionListener {


    CanvasPanel _canvas = new CanvasPanel();
	
	
	public static void main(String[] args) 
	{
		FreePaint FreePaint = new FreePaint();
		JFrame frame = new JFrame();
		frame.setContentPane(FreePaint);
		frame.setTitle("FreePaint");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		
	}

	public FreePaint() {
       //VERY temporary UI setup, most of the tools are unimplemented atm.
        JRadioButton pencilButton = new JRadioButton("Pencil", true);
        pencilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setTool(Tool.PENCIL);
            }});
        
        JRadioButton rectangleButton = new JRadioButton("Rectangle", true);
        rectangleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setShape(Shape.RECT);
            }});
        
        JRadioButton circleButton = new JRadioButton("Oval");
        circleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setShape(Shape.OVAL);
            }});
                
        JRadioButton lineButton = new JRadioButton("Line");
        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setShape(Shape.LINE);
            }});
            
        ButtonGroup toolGroup = new ButtonGroup();
        toolGroup.add(pencilButton);
        toolGroup.add(rectangleButton);
        toolGroup.add(circleButton);
        toolGroup.add(lineButton);
        
        
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(4,1));
        toolPanel.add(pencilButton);
        toolPanel.add(rectangleButton);
        toolPanel.add(circleButton);
        toolPanel.add(lineButton);
        
        
        JRadioButton redButton = new JRadioButton("Red", true);
        redButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setColor(Color.RED);
            }});
            
        JRadioButton greenButton = new JRadioButton("Green");
        greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setColor(Color.GREEN);
            }});
            
        JRadioButton blueButton = new JRadioButton("Blue");
        blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _canvas.setColor(Color.BLUE);
            }});
            
        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(redButton);
        colorGroup.add(greenButton);
        colorGroup.add(blueButton);
        
        
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(3,1));
        colorPanel.add(redButton);
        colorPanel.add(greenButton);
        colorPanel.add(blueButton);
        
       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(toolPanel);
        buttonPanel.add(colorPanel);
        buttonPanel.add(Box.createHorizontalGlue());
        
       
        setLayout(new BorderLayout(5,5));
        add(buttonPanel, BorderLayout.EAST);
        add(_canvas, BorderLayout.CENTER);
		
		
		
		
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {

	}
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
	}
}
