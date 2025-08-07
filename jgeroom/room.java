import gmaths.*;
import com.jogamp.opengl.*;
import java.util.ArrayList;
import java.util.List;

public class room {

  private List<Model> walls;
  private Model[] posters;
  private Camera camera;
  private Light light;

  private float size = 16f;

  public room(GL3 gl, Camera c, Light light) {

    this.camera = c;
    this.light = light;

    walls = new ArrayList<>();
    walls.add(makeFloor(gl));
    walls.add(makeBackWall(gl));
    walls.add(makeRightWall(gl));
    addLeftWallWithWindow(gl);

    posters = new Model[3];
    posters[0] = makePoster(gl, -4f, 5f, new Vec3(1f, 0f, 0f)); // Poster 1 - red
    posters[1] = makePoster(gl, 0f, 5f, new Vec3(0f, 1f, 0f));  // Poster 2 - green
    posters[2] = makePoster(gl, 4f, 5f, new Vec3(0f, 0f, 1f));  // Poster 3 - blue
  }

  // WALLS --------------------
  private Model makeFloor(GL3 gl) {
    Material m = new Material(new Vec3(0.8f,0.8f,0.8f), new Vec3(0.8f,0.8f,0.8f), new Vec3(0.3f,0.3f,0.3f), 32f);
    return createQuadRepeat(gl, "floor", Mat4Transform.scale(size,1f,size), 8f, 8f, m);
  }

  private Model makeBackWall(GL3 gl) {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(size,1f,size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), m);
    Material wallMat = new Material(new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), new Vec3(0.3f,0.3f,0.3f), 32f);
    return createQuad(gl, "backWall", m, wallMat);
  }

  private Model makeRightWall(GL3 gl) {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(size,1f,size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), m);
    Material wallMat = new Material(new Vec3(0.9f,0.9f,0.9f), new Vec3(0.9f,0.9f,0.9f), new Vec3(0.3f,0.3f,0.3f), 32f);
    return createQuadRepeat(gl, "rightWall", m, 4f, 4f, wallMat);
  }
  
  private void addLeftWallWithWindow(GL3 gl) {
    float border = 4f;
    float windowWidth = size - 2 * border;
    float windowHeight = size - 2 * border;

    Mat4 m;

    // Left vertical strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(border, 1f, size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, -size * 0.5f + border * 0.5f), m);
    Material wallMat = new Material(new Vec3(0.9f,0.9f,0.9f), new Vec3(0.9f,0.9f,0.9f), new Vec3(0.3f,0.3f,0.3f), 32f);
    walls.add(createQuad(gl, "leftWallSideL", m, wallMat));

    // Right vertical strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(border, 1f, size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, size * 0.5f - border * 0.5f), m);
    walls.add(createQuad(gl, "leftWallSideR", m, wallMat));

    // Top strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, border), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size - border * 0.5f, 0), m);
    walls.add(createQuad(gl, "leftWallTop", m, wallMat));

    // Bottom strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, border), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, border * 0.5f, 0), m);
    walls.add(createQuad(gl, "leftWallBottom", m, wallMat));

    // Outside view
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, windowHeight), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f + 0.1f, size * 0.5f, 0), m);
    Material windowMat = new Material(new Vec3(0.5f,0.7f,1f), new Vec3(0.5f,0.7f,1f), new Vec3(0.3f,0.3f,0.3f), 32f);
    walls.add(createQuad(gl, "windowView", m, windowMat));
  }

// POSTERS ------------------
  private Model makePoster(GL3 gl, float x, float y, Vec3 colour) {
    Material material = new Material(colour, colour, new Vec3(0f,0f,0f), 32.0f);

    // Transformation: scale first, then translate slightly backward
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(3f, 1f, 4f), m);               // Poster size/aspect
    m = Mat4.multiply(Mat4Transform.translate(x, y, -0.1f), m);         // Push poster slightly away from camera

    return createQuad(gl, "poster", m, material);
  }


  private Model createQuad(GL3 gl, String name, Mat4 modelMatrix) {
    Material mat = new Material(new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), new Vec3(0.3f,0.3f,0.3f), 32f);
    return createQuad(gl, name, modelMatrix, mat);
  }

  private Model createQuad(GL3 gl, String name, Mat4 modelMatrix, Material mat) {
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_0t.txt");
    return new Model(name, mesh, modelMatrix, shader, mat, light, camera);
  }

  private Model createQuadRepeat(GL3 gl, String name, Mat4 modelMatrix, float repeatU, float repeatV, Material mat) {
    float[] verts = {
      -0.5f, 0.0f, -0.5f,  0.0f,1.0f,0.0f,  0.0f, repeatV,
      -0.5f, 0.0f,  0.5f,  0.0f,1.0f,0.0f,  0.0f, 0.0f,
       0.5f, 0.0f,  0.5f,  0.0f,1.0f,0.0f,  repeatU, 0.0f,
       0.5f, 0.0f, -0.5f,  0.0f,1.0f,0.0f,  repeatU, repeatV
    };
    Mesh mesh = new Mesh(gl, verts, TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_0t.txt");
    return new Model(name, mesh, modelMatrix, shader, mat, light, camera);
  }

  public void render(GL3 gl) {
    for (Model m : walls) {
      m.render(gl);
    }
    for (Model p : posters) {
      p.render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (Model m : walls) m.dispose(gl);
    for (Model p : posters) p.dispose(gl);
  }
}
