import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

public class Model {
  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Light light;
  private Camera camera;
  private Texture texture1;
  private Texture texture2;

  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material,
               Light light, Camera camera, Texture texture1) {
    this(name, mesh, modelMatrix, shader, material, light, camera, texture1, null);
  }

  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material,
               Light light, Camera camera, Texture texture1, Texture texture2) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.light = light;
    this.camera = camera;
    this.texture1 = texture1;
    this.texture2 = texture2;
  }

  public void render(GL3 gl) {
    shader.use(gl);

    Mat4 view = camera.getViewMatrix();
    Mat4 proj = camera.getPerspectiveMatrix();
    Mat4 mv = Mat4.multiply(view, modelMatrix);
    Mat4 mvp = Mat4.multiply(proj, mv);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvp.toFloatArrayForGLSL());
    shader.setVec3(gl, "viewPos", camera.getPosition());

    if (light != null) {
      shader.setVec3(gl, "light.position", light.getPosition());
      Material lm = light.getMaterial();
      if (lm != null) {
        shader.setVec3(gl, "light.ambient", lm.getAmbient());
        shader.setVec3(gl, "light.specular", lm.getSpecular());
      }
      shader.setVec3(gl, "light.diffuse", light.getLightColour());
    }

    if (material != null) {
      shader.setVec3(gl, "material.ambient", material.getAmbient());
      shader.setVec3(gl, "material.diffuse", material.getDiffuse());
      shader.setVec3(gl, "material.specular", material.getSpecular());
      shader.setFloat(gl, "material.shininess", material.getShininess());
    }

    if (texture1 != null) {
      gl.glActiveTexture(GL.GL_TEXTURE0);
      texture1.bind(gl);
      shader.setInt(gl, "first_texture", 0);
    }
    if (texture2 != null) {
      gl.glActiveTexture(GL.GL_TEXTURE1);
      texture2.bind(gl);
      shader.setInt(gl, "second_texture", 1);
    }

    mesh.render(gl);

    if (texture1 != null) texture1.unbind(gl);
    if (texture2 != null) texture2.unbind(gl);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);
  }
}
