package aug.forgemaster;

import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.entity.GreekFireballEntityRenderer;
import aug.forgemaster.entity.ModEntities;
import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.item.ModItems;
import aug.forgemaster.particle.GreekFireParticle;
import aug.forgemaster.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Objects;

public class ForgemasterClient implements ClientModInitializer {
    public static final int[] CUBE_INDICES = {
            0, 1, 2, 3,
            1, 5, 6, 2,
            5, 4, 7, 6,
            4, 0, 3, 7,
            1, 0, 4, 5,
            3, 2, 6, 7
    };
    public static final Vector3f[] VERTICES = {
            new Vector3f(0, 1, 0),
            new Vector3f(1, 1, 0),
            new Vector3f(1, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0, 1, 1),
            new Vector3f(1, 1, 1),
            new Vector3f(1, 0, 1),
            new Vector3f(0, 0, 1)
    };
    public static final Vector2f[] TEX_COORDS = {
            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),

            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),

            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),

            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),

            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),

            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0)
    };
    public static final Vector3f[] NORMALS = {
            new Vector3f(0, 0, -1),
            new Vector3f(1, 0, 0),
            new Vector3f(0, 0, 1),
            new Vector3f(-1, 0, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, -1, 0),
    };

    public static void renderCube(int color, VertexConsumer consumer, MatrixStack.Entry entry, int light) {
        for (int i = 0, k = 0; i < 6; i++) {
            Vector3f normal = NORMALS[i];

            for (int j = 0; j < 4; j++, k++) {
                Vector2f tc = TEX_COORDS[k];
                Vector3f vertex = VERTICES[CUBE_INDICES[k]];

                consumer.vertex(entry, vertex.x / 2 - 0.25f, vertex.y / 2 - 0.25f, vertex.z / 2 - 0.25f)
                        .color(color)
                        .texture(tc.x, tc.y)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(light)
                        .normal(entry, normal.x, normal.y, normal.z);
            }
        }
    }

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEK_FIRE, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.GREEK_FIREBALL, GreekFireballEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.GREEK_FIRE, GreekFireParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIRE_SWEEP, SweepAttackParticle.Factory::new);
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            for (Entity entity : context.world().getEntities()) {
                if (entity instanceof PlayerEntity player && player.getActiveItem().isOf(ModItems.ATTACCA)) {
                    MatrixStack matrices = new MatrixStack();
                    float tickDelta = context.tickCounter().getTickDelta(true);

                    double x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
                    double y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
                    double z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

                    Vec3d origin = new Vec3d(x, y + entity.getStandingEyeHeight(), z).add(player.getRotationVec(tickDelta)).add(0, 0.25f, 0);

                    matrices.translate((float) -context.camera().getPos().x, (float) -context.camera().getPos().y, (float) -context.camera().getPos().z);
                    matrices.translate(origin.x, origin.y, origin.z);

                    GreekFireballEntityRenderer.render(player.age, MathHelper.clamp((player.getItemUseTime() + tickDelta) / 3, 0, AttaccaItem.MAX_FIREBALL_CHARGE) / AttaccaItem.MAX_FIREBALL_CHARGE, tickDelta, Objects.requireNonNull(context.consumers()), matrices);
                }
            }
        });
    }
}
