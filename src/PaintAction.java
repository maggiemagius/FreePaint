import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class PaintAction {
	protected PaintPreferences _prefs;
	
	public PaintAction(PaintPreferences prefs) {
		_prefs = prefs;
	}
	public abstract void render(Graphics2D graphics);
	public abstract void mouseDragged(MouseEvent e);
	public abstract void mouseReleased(MouseEvent e);
}
