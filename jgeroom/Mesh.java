import java.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.common.nio.Buffers;

public class Mesh {
  private int vao;
  private int vbo;
  private int ebo;
  private int vertexCount;

  public Mesh(GL3 gl, float[] vertices, int[] indices) {
    int[] tmp = new int[1];
    gl.glGenVertexArrays(1, tmp, 0);
    vao = tmp[0];
    gl.glBindVertexArray(vao);

    gl.glGenBuffers(1, tmp, 0);
    vbo = tmp[0];
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, fb, GL.GL_STATIC_DRAW);

    gl.glGenBuffers(1, tmp, 0);
    ebo = tmp[0];
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebo);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length * Integer.BYTES, ib, GL.GL_STATIC_DRAW);

    int stride = 8 * Float.BYTES;
    gl.glEnableVertexAttribArray(0);
    gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, stride, 0);
    gl.glEnableVertexAttribArray(1);
    gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, stride, 3 * Float.BYTES);
    gl.glEnableVertexAttribArray(2);
    gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, stride, 6 * Float.BYTES);

    gl.glBindVertexArray(0);
    vertexCount = indices.length;
  }

  public void render(GL3 gl) {
    gl.glBindVertexArray(vao);
    gl.glDrawElements(GL.GL_TRIANGLES, vertexCount, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    int[] tmp = new int[1];
    tmp[0] = vbo;
    gl.glDeleteBuffers(1, tmp, 0);
    tmp[0] = ebo;
    gl.glDeleteBuffers(1, tmp, 0);
    tmp[0] = vao;
    gl.glDeleteVertexArrays(1, tmp, 0);
  }
}
