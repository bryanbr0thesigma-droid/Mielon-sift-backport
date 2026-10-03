package mielon.thesift.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** A flat mist quad lying on the surface of an ichor source. */
public final class IchorSurfaceParticle extends TextureSheetParticle {
   private final SpriteSet sprites;
   private final float surfaceYaw;

   private IchorSurfaceParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
      super(level, x, y, z);
      this.sprites = sprites;
      this.pickSprite(sprites);
      this.surfaceYaw = this.random.nextFloat() * (float) (Math.PI * 2);
      this.quadSize = 0.12F + this.random.nextFloat() * 0.12F;
      this.lifetime = 40 + this.random.nextInt(31);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.friction = 1.0F;
      this.xd = 0.0;
      this.yd = 0.0;
      this.zd = 0.0;
      this.setAlpha(0.0F);
      this.setSpriteFromAge(sprites);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.removed) {
         float progress = (float)this.age / this.lifetime;
         this.setAlpha((float)Math.sin(Math.PI * progress) * 0.55F);
         this.setSpriteFromAge(this.sprites);
      }
   }

   @Override
   public void render(VertexConsumer buffer, Camera camera, float partialTick) {
      Vec3 cameraPos = camera.getPosition();
      float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - cameraPos.x());
      float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - cameraPos.y());
      float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - cameraPos.z());
      Quaternionf rotation = new Quaternionf().rotationY(this.surfaceYaw).rotateX((float) (-Math.PI / 2));
      Vector3f[] corners = new Vector3f[]{
         new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
      };
      float size = this.getQuadSize(partialTick);

      for (Vector3f corner : corners) {
         corner.rotate(rotation);
         corner.mul(size);
         corner.add(x, y, z);
      }

      float u0 = this.getU0();
      float u1 = this.getU1();
      float v0 = this.getV0();
      float v1 = this.getV1();
      int light = this.getLightColor(partialTick);
      buffer.vertex(corners[0].x(), corners[0].y(), corners[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buffer.vertex(corners[1].x(), corners[1].y(), corners[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buffer.vertex(corners[2].x(), corners[2].y(), corners[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buffer.vertex(corners[3].x(), corners[3].y(), corners[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
   }

   @Override
   public int getLightColor(float partialTick) {
      return 15728880;
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public static final class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Provider(SpriteSet sprites) {
         this.sprites = sprites;
      }

      @Override
      public IchorSurfaceParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
         return new IchorSurfaceParticle(level, x, y, z, this.sprites);
      }
   }
}
