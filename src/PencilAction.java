import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PencilAction extends PaintAction {
	private ArrayList<Point> _points;
	
	public PencilAction(PaintPreferences prefs) {
		super(prefs);
		_points = new ArrayList<Point>();
	}
	
	public void render(Graphics2D graphics) {
		if (_points.size() < 1) {
			return;
		}
		
		graphics.setColor(_prefs.color);
		graphics.setPaintMode();
		
		//TODO: Line Thickness settings
		graphics.setStroke(new BasicStroke(_prefs.thickness));

		int size = _points.size();
		if (size == 1) {
			Point current = _points.get(0);
			graphics.drawLine(current.x, current.y, current.x, current.y);
			return;
		}
		
		for (int i = 1; i < size; i++) {
			Point prev = _points.get(i - 1);
			Point current = _points.get(i);
			graphics.drawLine(prev.x, prev.y, current.x, current.y);
		}
	}

	public void mouseDragged(MouseEvent e) {
		tryAddPoint(e);
	}

	public void mouseReleased(MouseEvent e) {
		tryAddPoint(e);
	}
	
	private void tryAddPoint(MouseEvent e) {
		Point current = e.getPoint();
		_points.add(current);
	}
}
