import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyMouseInput implements MouseListener, MouseMotionListener {
  private final Camera camera;
  private int lastX;
  private int lastY;
  private boolean dragging = false;

  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    dragging = true;
    lastX = e.getX();
    lastY = e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    dragging = false;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!dragging) return;
    int x = e.getX();
    int y = e.getY();
    int dx = x - lastX;
    int dy = y - lastY;
    camera.updateYawPitch(dx * camera.MOUSE_SPEED * 0.1f, -dy * camera.MOUSE_SPEED * 0.1f);
    lastX = x;
    lastY = y;
  }

  @Override public void mouseMoved(MouseEvent e) {}
  @Override public void mouseClicked(MouseEvent e) {}
  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {}
}
