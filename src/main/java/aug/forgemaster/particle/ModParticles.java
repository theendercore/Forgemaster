package aug.forgemaster.particle;

import aug.forgemaster.Forgemaster;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {

    public static final ParticleType<GreekFireParticleEffect> GREEK_FIRE = Registry.register(Registries.PARTICLE_TYPE, Forgemaster.id("greek_fire"), FabricParticleTypes.complex(false, GreekFireParticleEffect.CODEC, GreekFireParticleEffect.PACKET_CODEC));

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Particles for " + Forgemaster.MOD_ID);
    }
}
