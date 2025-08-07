/* I declare that this code is my own work */
/* Author: Jack Edwards - jgedwards1@sheff.ac.uk */

import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    GLProfile profile = GLProfile.get(GLProfile.GL3);
    GLCapabilities capabilities = new GLCapabilities(profile);
    GLCanvas canvas = new GLCanvas(capabilities);

    RoomScene renderer = new RoomScene();
    canvas.addGLEventListener(renderer);
    canvas.setSize(800, 800);

    // Input handlers for moving the camera
    MyKeyboardInput keyboardInput = new MyKeyboardInput(renderer.getCamera());
    MyMouseInput mouseInput = new MyMouseInput(renderer.getCamera());
    canvas.addKeyListener(keyboardInput);
    canvas.addMouseListener(mouseInput);
    canvas.addMouseMotionListener(mouseInput);
    canvas.setFocusable(true);
    canvas.requestFocusInWindow();

    final JFrame frame = new JFrame("Room Scene");
    frame.getContentPane().add(canvas);
    frame.setSize(frame.getContentPane().getPreferredSize());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    final FPSAnimator animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
}
