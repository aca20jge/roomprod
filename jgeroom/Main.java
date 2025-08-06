/* I declare that this code is my own work */
/* Author: Your Name - your.email@domain.com */

import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureLoader;

import javax.swing.*;
import java.io.File;

public class Main {
  public static void main(String[] args) {
    GLProfile profile = GLProfile.get(GLProfile.GL3);
    GLCapabilities capabilities = new GLCapabilities(profile);
    GLCanvas canvas = new GLCanvas(capabilities);

    RoomScene renderer = new RoomScene();
    canvas.addGLEventListener(renderer);
    canvas.setSize(800, 800);

    final JFrame frame = new JFrame("3D Room Scene");
    frame.getContentPane().add(canvas);
    frame.setSize(frame.getContentPane().getPreferredSize());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    final FPSAnimator animator = new FPSAnimator(canvas, 60);
    animator.start();

  }
}
