package aug.forgemaster.item;

import aug.forgemaster.Forgemaster;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItemComponentTypes {
    public static final ComponentType<Integer> ATTACCA_CHARGE = Registry.register(Registries.DATA_COMPONENT_TYPE, Forgemaster.id("attacca_charge"), ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build());

    public static void register() {
    }
}
