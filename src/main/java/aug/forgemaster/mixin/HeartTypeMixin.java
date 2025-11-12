package aug.forgemaster.mixin;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.effect.ScorchedEffect;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.HeartType.class)
public abstract class HeartTypeMixin {
    @Invoker("<init>")
    private static InGameHud.HeartType invokeNew(
            String name, int ordinal,
            Identifier fullTexture,
            Identifier fullBlinkingTexture,
            Identifier halfTexture,
            Identifier halfBlinkingTexture,
            Identifier hardcoreFullTexture,
            Identifier hardcoreFullBlinkingTexture,
            Identifier hardcoreHalfTexture,
            Identifier hardcoreHalfBlinkingTexture
    ) {
        throw new AssertionError();
    }

    @Shadow
    @Final
    @Mutable
    private static InGameHud.HeartType[] field_33952;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void clinit(CallbackInfo ci) {
        InGameHud.HeartType[] arr = new InGameHud.HeartType[field_33952.length + 1];
        System.arraycopy(field_33952, 0, arr, 0, field_33952.length);
        ScorchedEffect.HEART_TYPE = arr[field_33952.length] = invokeNew(
                "FORGEMASTER_SCORCHED", field_33952.length,
                Forgemaster.id("hud/heart/scorched_full"),
                Forgemaster.id("hud/heart/scorched_full_blinking"),
                Forgemaster.id("hud/heart/scorched_half"),
                Forgemaster.id("hud/heart/scorched_half_blinking"),
                Forgemaster.id("hud/heart/scorched_hardcore_full"),
                Forgemaster.id("hud/heart/scorched_hardcore_full_blinking"),
                Forgemaster.id("hud/heart/scorched_hardcore_half"),
                Forgemaster.id("hud/heart/scorched_hardcore_half_blinking")
        );
        field_33952 = arr;
    }

    @Inject(
            method = "fromPlayerState",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void fromPlayerState(PlayerEntity player, CallbackInfoReturnable<InGameHud.HeartType> cir) {
        if (player.hasStatusEffect(ModEffects.SCORCHED)) {
            cir.setReturnValue(ScorchedEffect.HEART_TYPE);
        }
    }
}
