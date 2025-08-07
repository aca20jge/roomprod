import com.jogamp.opengl.*;
import gmaths.*;

public class RoomScene implements GLEventListener {

  private Camera camera;
  private Light light;
  private room myRoom;

  private final float aspect = 1f; // will update in reshape
  private final Vec3 backgroundColor = new Vec3(0.1f, 0.1f, 0.1f);

  @Override
  public void init(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    System.out.println("RoomScene init");

    // Basic GL setup
    gl.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);

    // Camera (using your class)

    // You can also move it higher to look down into the room:
    Vec3 position = new Vec3(0f, 12f, 25f);
    Vec3 target = new Vec3(0f, 5f, 0f);

    Vec3 up = new Vec3(0f, 1f, 0f);
    camera = new Camera(position, target, up);
    
    // Perspective projection
    float fovy = (float) Math.toRadians(60.0);
    float near = 0.1f;
    float far = 100f;
    Mat4 perspective = Mat4Transform.perspective(fovy, aspect, near, far);
    camera.setPerspectiveMatrix(perspective);

    // Light
    light = new Light(gl);
    light.setPosition(new Vec3(2f, 15f, 2f));
    light.setCamera(camera);
    light.setMaterial(new Material(new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), 32f));
    light.setLightColour(new Vec3(1, 1, 1));

    // Build the room scene without textures
    myRoom = new room(gl, camera, light);
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // Update camera view matrix
    camera.getViewMatrix(); // your class auto-updates this

    // Render light and scene
    light.setCamera(camera);
    light.render(gl);
    myRoom.render(gl);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    System.out.println("Reshape - width: " + width + ", height: " + height);

    float newAspect = (float) width / (float) height;
    Mat4 perspective = Mat4Transform.perspective((float) Math.toRadians(60.0), newAspect, 0.1f, 100f);
    camera.setPerspectiveMatrix(perspective);
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    myRoom.dispose(gl);
  }

  public Camera getCamera() {
    return camera;
  }
}
