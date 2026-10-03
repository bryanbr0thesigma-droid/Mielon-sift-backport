package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.joml.Matrix4f;

/** Procedural mesh for the rift: surface quads and soft glow halo built from the animated cube model. */
public final class RiftMesh {
   private final List<Quad> surface = new ArrayList<>();
   private final List<Quad> glow = new ArrayList<>();
   private final List<float[][]> boxes = new ArrayList<>();

   public RiftMesh(List<RiftAssetModel.Box> input) {
      for (RiftAssetModel.Box b : input) {
         this.boxes.add(new float[][]{{b.x0(), b.y0(), b.z0()}, {b.x1(), b.y1(), b.z1()}});
      }

      for (float[][] b : this.boxes) {
         for (int axis = 0; axis < 3; axis++) {
            for (int sign : new int[]{-1, 1}) {
               this.face(b, axis, sign);
            }
         }
      }
   }

   private boolean occupied(float[] p) {
      for (float[][] b : this.boxes) {
         if (p[0] >= b[0][0] - 1.0E-5F
            && p[0] <= b[1][0] + 1.0E-5F
            && p[1] >= b[0][1] - 1.0E-5F
            && p[1] <= b[1][1] + 1.0E-5F
            && p[2] >= b[0][2] - 1.0E-5F
            && p[2] <= b[1][2] + 1.0E-5F) {
            return true;
         }
      }

      return false;
   }

   private List<Float> cuts(float[][] b, int axis) {
      TreeSet<Float> c = new TreeSet<>();
      c.add(b[0][axis]);
      c.add(b[1][axis]);

      for (float[][] other : this.boxes) {
         for (float[] p : other) {
            if (p[axis] > b[0][axis] && p[axis] < b[1][axis]) {
               c.add(p[axis]);
            }
         }
      }

      return new ArrayList<>(c);
   }

   private void face(float[][] b, int a, int sign) {
      int u = (a + 1) % 3;
      int v = (a + 2) % 3;
      float plane = b[sign < 0 ? 0 : 1][a];
      List<Float> us = this.cuts(b, u);
      List<Float> vs = this.cuts(b, v);

      for (int i = 1; i < us.size(); i++) {
         for (int j = 1; j < vs.size(); j++) {
            float u0 = us.get(i - 1);
            float u1 = us.get(i);
            float v0 = vs.get(j - 1);
            float v1 = vs.get(j);
            float[] p = new float[3];
            p[a] = plane + sign * 2.0E-4F;
            p[u] = (u0 + u1) / 2.0F;
            p[v] = (v0 + v1) / 2.0F;
            if (!this.occupied(p)) {
               quad(this.surface, a, sign, plane, u0, u1, v0, v1, a == 2 ? 0 : 100, 255);
               if (a == 2) {
                  p[a] = plane - sign * 2.0E-4F;

                  for (int edge = 0; edge < 4; edge++) {
                     p[u] = (u0 + u1) / 2.0F;
                     p[v] = (v0 + v1) / 2.0F;
                     if (edge < 2) {
                        p[u] = edge == 0 ? u0 - 2.0E-4F : u1 + 2.0E-4F;
                     } else {
                        p[v] = edge == 2 ? v0 - 2.0E-4F : v1 + 2.0E-4F;
                     }

                     if (!this.occupied(p)) {
                        float width = Math.min(0.032F, Math.min(u1 - u0, v1 - v0) * 0.45F);
                        strip(this.surface, a, sign, plane + sign * 0.005F, u0, u1, v0, v1, edge, 0.0F, width, 180, 255);
                        strip(this.surface, a, sign, plane + sign * 0.003F, u0, u1, v0, v1, edge, width, width * 2.0F, 100, 255);

                        for (int k = 0; k < 10; k++) {
                           strip(
                              this.glow,
                              a,
                              sign,
                              plane + sign * 0.006F,
                              u0,
                              u1,
                              v0,
                              v1,
                              edge,
                              -(k + 1) * 0.065F,
                              -k * 0.065F,
                              255,
                              (int)(220.0 * Math.pow(1.0 - k / 10.0, 2.0))
                           );
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void strip(List<Quad> out, int a, int sign, float p, float u0, float u1, float v0, float v1, int edge, float from, float to, int mode, int alpha) {
      switch (edge) {
         case 0:
            quad(out, a, sign, p, u0 + from, u0 + to, v0, v1, mode, alpha);
            break;
         case 1:
            quad(out, a, sign, p, u1 - to, u1 - from, v0, v1, mode, alpha);
            break;
         case 2:
            quad(out, a, sign, p, u0, u1, v0 + from, v0 + to, mode, alpha);
            break;
         case 3:
            quad(out, a, sign, p, u0, u1, v1 - to, v1 - from, mode, alpha);
      }
   }

   private static void quad(List<Quad> out, int a, int sign, float plane, float u0, float u1, float v0, float v1, int mode, int alpha) {
      int u = (a + 1) % 3;
      int v = (a + 2) % 3;
      float[][] points = new float[4][3];
      float[][] uv = new float[][]{{u0, v0}, {u1, v0}, {u1, v1}, {u0, v1}};

      for (int i = 0; i < 4; i++) {
         int source = sign > 0 ? i : 3 - i;
         points[i][a] = plane;
         points[i][u] = uv[source][0];
         points[i][v] = uv[source][1];
      }

      out.add(new Quad(points, mode, alpha));
   }

   public int surfaceQuadCount() {
      return this.surface.size();
   }

   public void draw(VertexConsumer buffer, Matrix4f pose, boolean alongX, boolean halo) {
      for (Quad q : halo ? this.glow : this.surface) {
         for (float[] p : q.points) {
            buffer.vertex(pose, alongX ? p[0] : p[2], p[1], alongX ? p[2] : -p[0])
               .uv((p[0] + 4.5F) / 9.0F, 1.0F - (p[1] + 0.375F) / 4.25F)
               .color(q.mode, 255, 255, q.alpha)
               .endVertex();
         }
      }
   }

   private record Quad(float[][] points, int mode, int alpha) {
   }
}
