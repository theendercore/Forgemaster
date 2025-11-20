package aug.forgemaster.entity;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.ForgemasterClient;
import aug.forgemaster.item.AttaccaItem;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class GreekFireballEntityRenderer extends EntityRenderer<GreekFireballEntity> {
    public GreekFireballEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public static void render(int ticks, float charge, float tickDelta, VertexConsumerProvider consumers, MatrixStack matrices) {
        matrices.push();
        float time = (ticks + tickDelta) / 20;
        matrices.multiply(new Quaternionf().rotationXYZ(-time, time, 0));
        float scale = charge + 0.1f;
        matrices.scale(scale, scale, scale);

        ForgemasterClient.renderCube(AttaccaItem.getChargeColor(1 - charge), consumers.getBuffer(VeilRenderType.get(Forgemaster.id("greek_fireball"))), matrices.peek(), LightmapTextureManager.MAX_LIGHT_COORDINATE);

        matrices.pop();
    }

    @Override
    public void render(GreekFireballEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        render(entity.age, entity.charge(), tickDelta, vertexConsumers, matrices);
    }

    @Override
    public Identifier getTexture(GreekFireballEntity entity) {
        return null;
    }
}
