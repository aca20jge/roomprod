import gmaths.*;
import com.jogamp.opengl.*;

public class Light {
  private Vec3 position = new Vec3(0,0,0);
  private Vec3 lightColour = new Vec3(1,1,1);
  private Material material;
  private Camera camera;

  public Light(GL3 gl) {
    // no GL resources required for simple stub
  }

  public void setPosition(Vec3 p) { position = p; }
  public Vec3 getPosition() { return position; }

  public void setLightColour(Vec3 c) { lightColour = c; }
  public Vec3 getLightColour() { return lightColour; }

  public void setMaterial(Material m) { material = m; }
  public Material getMaterial() { return material; }

  public void setCamera(Camera c) { camera = c; }
  public Camera getCamera() { return camera; }

  public void render(GL3 gl) {
    // simple stub - no rendering of light geometry
  }
}
