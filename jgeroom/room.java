import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;
import java.util.ArrayList;
import java.util.List;

public class room {

  private List<Model> walls;
  private Model[] posters;
  private Camera camera;
  private Light light;

  // Textures
  private Texture t_floor, t_back, t_right, t_left, t_window;
  private Texture poster1Tex, poster2Tex, poster2Spec, poster3Tex;

  private float size = 16f;

  public room(GL3 gl, Camera c, Light light,
              Texture t_floor, Texture t_back, Texture t_right, Texture t_left, Texture t_window,
              Texture poster1Tex, Texture poster2Tex, Texture poster2Spec, Texture poster3Tex) {

    this.camera = c;
    this.light = light;
    this.t_floor = t_floor;
    this.t_back = t_back;
    this.t_right = t_right;
    this.t_left = t_left;
    this.t_window = t_window;
    this.poster1Tex = poster1Tex;
    this.poster2Tex = poster2Tex;
    this.poster2Spec = poster2Spec;
    this.poster3Tex = poster3Tex;

    walls = new ArrayList<>();
    walls.add(makeFloor(gl));
    walls.add(makeBackWall(gl));
    walls.add(makeRightWall(gl));
    addLeftWallWithWindow(gl);

    posters = new Model[3];
    posters[0] = makePoster(gl, -4f, 5f, poster1Tex, null);              // Poster 1 - no specular
    posters[1] = makePoster(gl, 0f, 5f, poster2Tex, poster2Spec);        // Poster 2 - with specular
    posters[2] = makePoster(gl, 4f, 5f, poster3Tex, null);               // Poster 3 - no specular
  }

  // WALLS --------------------
  private Model makeFloor(GL3 gl) {
    return createQuad(gl, "floor", t_floor, null,
      Mat4Transform.scale(size,1f,size));
  }

  private Model makeBackWall(GL3 gl) {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(size,1f,size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), m);
    return createQuad(gl, "backWall", t_back, null, m);
  }

  private Model makeRightWall(GL3 gl) {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(size,1f,size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), m);
    return createQuadRepeat(gl, "rightWall", t_right, null, m, 4f, 4f);
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
    walls.add(createQuad(gl, "leftWallSideL", t_left, null, m));

    // Right vertical strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(border, 1f, size), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, size * 0.5f - border * 0.5f), m);
    walls.add(createQuad(gl, "leftWallSideR", t_left, null, m));

    // Top strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, border), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size - border * 0.5f, 0), m);
    walls.add(createQuad(gl, "leftWallTop", t_left, null, m));

    // Bottom strip
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, border), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f, border * 0.5f, 0), m);
    walls.add(createQuad(gl, "leftWallBottom", t_left, null, m));

    // Outside view
    m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(windowWidth, 1f, windowHeight), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(-90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
    m = Mat4.multiply(Mat4Transform.translate(size * 0.5f + 0.1f, size * 0.5f, 0), m);
    walls.add(createQuad(gl, "windowView", t_window, null, m));
  }

// POSTERS ------------------
  private Model makePoster(GL3 gl, float x, float y, Texture tex, Texture specTex) {
    Material material = new Material(new Vec3(1f), new Vec3(1f), new Vec3(0.5f), 32.0f);

    // Transformation: scale first, then translate slightly backward
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(3f, 1f, 4f), m);               // Poster size/aspect
    m = Mat4.multiply(Mat4Transform.translate(x, y, -0.1f), m);         // Push poster slightly away from camera

    return createQuad(gl, "poster", tex, specTex, m);
  }


  private Model createQuad(GL3 gl, String name, Texture t1, Texture t2, Mat4 modelMatrix) {
    Material mat = new Material(new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), new Vec3(0.3f,0.3f,0.3f), 32f);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader;

    if (t2 != null) {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
      return new Model(name, mesh, modelMatrix, shader, mat, light, camera, t1, t2);
    } else {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
      return new Model(name, mesh, modelMatrix, shader, mat, light, camera, t1);
    }
  }

  private Model createQuadRepeat(GL3 gl, String name, Texture t1, Texture t2, Mat4 modelMatrix, float repeatU, float repeatV) {
    Material mat = new Material(new Vec3(1f,1f,1f), new Vec3(1f,1f,1f), new Vec3(0.3f,0.3f,0.3f), 32f);
    float[] verts = {
      -0.5f, 0.0f, -0.5f,  0.0f,1.0f,0.0f,  0.0f, repeatV,
      -0.5f, 0.0f,  0.5f,  0.0f,1.0f,0.0f,  0.0f, 0.0f,
       0.5f, 0.0f,  0.5f,  0.0f,1.0f,0.0f,  repeatU, 0.0f,
       0.5f, 0.0f, -0.5f,  0.0f,1.0f,0.0f,  repeatU, repeatV
    };
    Mesh mesh = new Mesh(gl, verts, TwoTriangles.indices.clone());
    Shader shader;
    if (t2 != null) {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
      return new Model(name, mesh, modelMatrix, shader, mat, light, camera, t1, t2);
    } else {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
      return new Model(name, mesh, modelMatrix, shader, mat, light, camera, t1);
    }
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
