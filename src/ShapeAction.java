import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class ShapeAction extends PaintAction {
	private Shape _shape;
	private Point _start;
	private Point _end;
	
	public ShapeAction(Shape shape, Point start, PaintPreferences prefs) {
		super(prefs);
		_start = start;
		_shape = shape;
	}
	
	public void render(Graphics2D graphics) {
		graphics.setColor(this._prefs.color);
		graphics.setStroke(new BasicStroke(this._prefs.thickness));
		
		switch (_shape) {
		case RECT:
			Rectangle2D.Float rect = new Rectangle2D.Float(
					Math.min(_start.x, _end.x),
					Math.min(_start.y, _end.y),
					Math.abs(_start.x - _end.x),
					Math.abs(_start.y - _end.y));
			graphics.draw(rect);
			break;
		case OVAL:
			Ellipse2D.Float circle = new Ellipse2D.Float(
					Math.min(_start.x, _end.x),
					Math.min(_start.y, _end.y),
					Math.abs(_start.x - _end.x),
					Math.abs(_start.y - _end.y));
			graphics.draw(circle);
			break;
		case LINE:
			Line2D.Float line = new Line2D.Float(
					_start.x,
					_start.y,
					_end.x,
					_end.y);
			graphics.draw(line);
			break;
		}
	}

	public void mouseDragged(MouseEvent e) {
		_end = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
		_end = e.getPoint();
	}
}
