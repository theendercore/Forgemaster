package aug.forgemaster.item;

import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.enchantment.ModEnchantmentEffects;
import aug.forgemaster.entity.GreekFireballEntity;
import aug.forgemaster.particle.GreekFireParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class AttaccaItem extends SwordItem implements DualModelItem {
    public static final int MIN_FIREBALL_CHARGE = 4, MAX_FIREBALL_CHARGE = 16, MAX_CAPACITY = 32;

    public AttaccaItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public Identifier worldModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_3d");
    }

    @Override
    public Identifier guiModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_2d");
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) {
            Vec3d origin = user.getEyePos().add(user.getRotationVector()).add(0, 0.25f, 0);
            Vec3d pos = origin.addRandom(user.getRandom(), 1.5f);
            world.addParticle(
                    new GreekFireParticleEffect(getChargeColor(1 - MathHelper.clamp((72000f - remainingUseTicks) / 3 / MAX_FIREBALL_CHARGE, 0, 1))),
                    true,
                    pos.x, pos.y, pos.z,
                    (origin.x - pos.x) / 4, (origin.y - pos.y) / 4, (origin.z - pos.z) / 4
            );
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient && selected && stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) >= MAX_CAPACITY) {
            world.addParticle(
                    ParticleTypes.FLAME,
                    entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1),
                    0, 0, 0
            );
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) >= AttaccaItem.MAX_CAPACITY) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.SPARKED, 300));
            stack.set(ModItemComponentTypes.ATTACCA_CHARGE, 0);
        }

        target.setOnFireFor(100);

        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);

        if (charge >= MIN_FIREBALL_CHARGE && ModEnchantmentEffects.getAffannato(stack) > 0) {
            return 72000;
        }

        return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (getMaxUseTime(stack, user) == 0) {
            return TypedActionResult.pass(stack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);
        int usedCharge = MathHelper.clamp((72000 - remainingUseTicks) / 3, 0, Math.min(charge, MAX_FIREBALL_CHARGE));

        if (usedCharge >= MIN_FIREBALL_CHARGE) {
            float strength = ModEnchantmentEffects.getAffannato(stack);

            if (strength > 0) {
                if (world.isClient) {
                    world.playSoundFromEntity(user, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1, 1);
                } else {
                    Vec3d pos = user.getEyePos().add(user.getRotationVector()).add(0, 0.25f, 0);
                    GreekFireballEntity fireball = new GreekFireballEntity(pos.x, pos.y, pos.z, user.getRotationVector(), world);
                    fireball.strength = strength * usedCharge;
                    world.spawnEntity(fireball);
                    stack.set(ModItemComponentTypes.ATTACCA_CHARGE, charge - usedCharge);
                }
            }
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) != 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);

        if (charge == 0 && super.isItemBarVisible(stack)) {
            return super.getItemBarStep(stack);
        }

        return MathHelper.clamp(Math.round(charge * 13f / MAX_CAPACITY), 0, 13);
    }

    public static int getChargeColor(float blend) {
        return 0xFF000000 | (MathHelper.lerp(blend, 225, 175) << 16) | (MathHelper.lerp(blend, 147, 42) << 8) | (MathHelper.lerp(blend, 71, 21));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);

        if (charge == 0) {
            return super.getItemBarColor(stack);
        }

        if (charge >= MAX_CAPACITY) {
            return getChargeColor((float) Math.sin(GLFW.glfwGetTime() * 4) / 2 + 0.5f);
        }

        return 0xFFF48522;
    }
}
