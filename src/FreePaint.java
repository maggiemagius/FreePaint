import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.FlowLayout;



public class FreePaint extends JPanel implements MouseListener, MouseMotionListener {

    private static JFrame _frame;
    private CanvasPanel _canvas;
    private JSpinner _brushSizeSpinner;
    private JSlider _redSlider;
    private JSlider _blueSlider;
    private JSlider _greenSlider;
    private JPanel _colorPreview;
    private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public static void main(String[] args) 
	{
		FreePaint FreePaint = new FreePaint();
		_frame = new JFrame();
		_frame.setContentPane(FreePaint);
		_frame.setTitle("FreePaint");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setResizable(false);
		_frame.pack();
		_frame.setVisible(true);
	}

	public FreePaint() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("200px"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		JPanel panel = new JPanel();
		add(panel, "1, 1, fill, fill");
		
		_canvas = new CanvasPanel();
		_canvas.setLayout(new GridLayout(1, 0, 0, 0));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(_canvas, GroupLayout.PREFERRED_SIZE, 463, GroupLayout.PREFERRED_SIZE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(_canvas, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE)
		);
		panel.setLayout(gl_panel);
		
		JPanel toolbox = new JPanel();
		add(toolbox, "2, 1, fill, fill");
		toolbox.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),
				RowSpec.decode("250px"),
				RowSpec.decode("75px:grow"),}));
		
		JPanel tools = new JPanel();
		toolbox.add(tools, "1, 1, fill, fill");
		tools.setLayout(new GridLayout(2, 2, 0, 0));
		
		JRadioButton rdbtnSquare = new JRadioButton("Square");
		buttonGroup.add(rdbtnSquare);
		tools.add(rdbtnSquare);
		rdbtnSquare.addActionListener((ActionEvent e) -> {
            _canvas.setTool(Tool.SHAPE);
            _canvas.setShape(Shape.RECT);
        });
		
		JRadioButton rdbtnOval = new JRadioButton("Oval");
		buttonGroup.add(rdbtnOval);
		tools.add(rdbtnOval);
		rdbtnOval.addActionListener((ActionEvent e) -> {
            _canvas.setTool(Tool.SHAPE);
            _canvas.setShape(Shape.OVAL);
        });
		
		JRadioButton rdbtnLine = new JRadioButton("Line");
		buttonGroup.add(rdbtnLine);
		tools.add(rdbtnLine);
		
		JRadioButton rdbtnPencil = new JRadioButton("Pencil");
		rdbtnPencil.setSelected(true);
		buttonGroup.add(rdbtnPencil);
		tools.add(rdbtnPencil);
		rdbtnPencil.addActionListener((ActionEvent e) -> {
                _canvas.setTool(Tool.PENCIL);
            });
		
		JRadioButton rdbtnEraser = new JRadioButton("Eraser");
		buttonGroup.add(rdbtnEraser);
		tools.add(rdbtnEraser);
		rdbtnEraser.addActionListener((ActionEvent e) -> {
            _canvas.setTool(Tool.ERASER);
        });
		rdbtnLine.addActionListener((ActionEvent e) -> {
            _canvas.setTool(Tool.SHAPE);
            _canvas.setShape(Shape.LINE);
        });
		
		JPanel undoRedoPanel = new JPanel();
		toolbox.add(undoRedoPanel, "1, 2, fill, fill");
		
		JButton btnUndo = new JButton("Undo");
		undoRedoPanel.add(btnUndo);
		btnUndo.addActionListener(
            (ActionEvent e) -> {
                _canvas.undo();
            });
		
		JButton btnRedo = new JButton("Redo");
		undoRedoPanel.add(btnRedo);
		btnRedo.addActionListener(
	            (ActionEvent e) -> {
	                _canvas.redo();
	            });
		
		_canvas.setUndoRedoButtons(btnUndo, btnRedo);
		
		JPanel paintPrefsPanel = new JPanel();
		toolbox.add(paintPrefsPanel, "1, 3, fill, fill");
		paintPrefsPanel.setLayout(new GridLayout(10, 0, 0, 2));
		
		JLabel lblBrushSize = new JLabel("Brush Size");
		lblBrushSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrushSize.setVerticalAlignment(SwingConstants.CENTER);
		paintPrefsPanel.add(lblBrushSize);
		
		_brushSizeSpinner = new JSpinner();
		_brushSizeSpinner.setModel(new SpinnerNumberModel(1, 1, 250, 1));
		paintPrefsPanel.add(_brushSizeSpinner);
		_brushSizeSpinner.addChangeListener((ChangeEvent e) -> { this.updatePreferences(); });
		
		JLabel lblRed = new JLabel("Red");
		lblRed.setForeground(new Color(128, 0, 0));
		lblRed.setHorizontalAlignment(SwingConstants.CENTER);
		paintPrefsPanel.add(lblRed);
		
		_redSlider = new JSlider();
		_redSlider.setMaximum(255);
		paintPrefsPanel.add(_redSlider);
		_redSlider.addChangeListener((ChangeEvent e) -> { this.updatePreferences(); });
		
		JLabel lblGreen = new JLabel("Green");
		lblGreen.setForeground(new Color(0, 128, 0));
		lblGreen.setHorizontalAlignment(SwingConstants.CENTER);
		paintPrefsPanel.add(lblGreen);
		
		_greenSlider = new JSlider();
		_greenSlider.setMaximum(255);
		paintPrefsPanel.add(_greenSlider);
		_greenSlider.addChangeListener((ChangeEvent e) -> { this.updatePreferences(); });
		
		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setForeground(new Color(0, 0, 160));
		lblBlue.setHorizontalAlignment(SwingConstants.CENTER);
		paintPrefsPanel.add(lblBlue);
		
		_blueSlider = new JSlider();
		_blueSlider.setMaximum(255);
		paintPrefsPanel.add(_blueSlider);
		_blueSlider.addChangeListener((ChangeEvent e) -> { this.updatePreferences(); });
		
		_colorPreview = new JPanel();
		paintPrefsPanel.add(_colorPreview);
		
		JPanel saveLoadPanel = new JPanel();
		toolbox.add(saveLoadPanel, "1, 4, fill, fill");
		
		JButton btnSave = new JButton("Save");
		saveLoadPanel.add(btnSave);
		btnSave.addActionListener((ActionEvent e) -> { _canvas.save(); });
		
		JButton btnLoad = new JButton("Load");
		saveLoadPanel.add(btnLoad);
		btnLoad.addActionListener((ActionEvent e) -> { _canvas.load(); });
		
		updatePreferences();

		
		
		
		
	}
	
	private void updatePreferences() {
		Color c = new Color(_redSlider.getValue(), _greenSlider.getValue(), _blueSlider.getValue());
		_canvas.setPreferences(new PaintPreferences((Integer)_brushSizeSpinner.getValue(), c));
		_colorPreview.setBackground(c);
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
