package aug.forgemaster.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class GreekFireParticle extends SpriteBillboardParticle {
    public final GreekFireParticleEffect params;
    public final SpriteProvider provider;

    public GreekFireParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, GreekFireParticleEffect params, SpriteProvider provider) {
        super(world, x, y, z);
        this.params = params;
        maxAge = 3;
        alpha = ((params.color() & 0xFF000000) >> 24) / 255f;
        setColor(((params.color() & 0xFF0000) >> 16) / 255f, ((params.color() & 0xFF00) >> 8) / 255f, (params.color() & 0xFF) / 255f);
        setVelocity(vx, vy, vz);
        this.provider = provider;
        setSpriteForAge(provider);
    }

    @Override
    protected int getBrightness(float tint) {
        return LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        setSpriteForAge(provider);
        super.tick();
    }

    @Environment(EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<GreekFireParticleEffect> {
        public Particle createParticle(GreekFireParticleEffect params, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GreekFireParticle(clientWorld, d, e, f, g, h, i, params, spriteProvider);
        }
    }
}
