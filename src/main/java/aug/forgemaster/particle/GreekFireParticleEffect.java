package aug.forgemaster.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record GreekFireParticleEffect(int color) implements ParticleEffect {
	public static final MapCodec<GreekFireParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(particleEffect -> particleEffect.color)
			).apply(instance, GreekFireParticleEffect::new)
	);
	public static final PacketCodec<RegistryByteBuf, GreekFireParticleEffect> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, effect -> effect.color,
            GreekFireParticleEffect::new
	);

	@Override
	public ParticleType<GreekFireParticleEffect> getType() {
		return ModParticles.GREEK_FIRE;
	}
}
