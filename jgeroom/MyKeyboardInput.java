import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyboardInput extends KeyAdapter {
  private final Camera camera;

  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W -> camera.keyboardInput(Camera.Movement.FORWARD);
      case KeyEvent.VK_S -> camera.keyboardInput(Camera.Movement.BACK);
      case KeyEvent.VK_A -> camera.keyboardInput(Camera.Movement.LEFT);
      case KeyEvent.VK_D -> camera.keyboardInput(Camera.Movement.RIGHT);
      case KeyEvent.VK_Q -> camera.keyboardInput(Camera.Movement.DOWN);
      case KeyEvent.VK_E -> camera.keyboardInput(Camera.Movement.UP);
      default -> camera.keyboardInput(Camera.Movement.NO_MOVEMENT);
    }
  }
}
