package aug.forgemaster.entity;

import aug.forgemaster.block.GreekFireBlock;
import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.particle.GreekFireParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GreekFireballEntity extends ExplosiveProjectileEntity {
    public float strength = 1;

    public GreekFireballEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public GreekFireballEntity(double x, double y, double z, World world) {
        super(ModEntities.GREEK_FIREBALL, x, y, z, world);
    }

    public GreekFireballEntity(double x, double y, double z, Vec3d velocity, World world) {
        super(ModEntities.GREEK_FIREBALL, x, y, z, velocity, world);
    }

    public GreekFireballEntity(LivingEntity owner, Vec3d velocity, World world) {
        super(ModEntities.GREEK_FIREBALL, owner, velocity, world);
    }

    public void explode() {
        if (!getWorld().isClient) {
            getWorld().createExplosion(this, getX(), getY(), getZ(), 2, false, World.ExplosionSourceType.NONE);

            float radius = strength / 4 + 2;
            int maxRadius = (int) Math.ceil(radius);

            for (int x = -maxRadius; x <= maxRadius; x++) {
                blocks:
                for (int z = -maxRadius; z <= maxRadius; z++) {
                    BlockPos pos = getBlockPos().add(x, 0, z);
                    BlockState state = getWorld().getBlockState(pos);

                    if (state.isSideSolid(getWorld(), pos, Direction.UP, SideShapeType.FULL) && state.isSolidBlock(getWorld(), pos)) {
                        while ((state = getWorld().getBlockState(pos)).isSolidBlock(getWorld(), pos) && state.isSideSolid(getWorld(), pos, Direction.UP, SideShapeType.FULL)) {
                            pos = pos.up();

                            if (!pos.isWithinDistance(getPos(), radius)) {
                                continue blocks;
                            }
                        }
                    } else {
                        while (!(state = getWorld().getBlockState(pos.down())).isSolidBlock(getWorld(), pos.down()) && !state.isSideSolid(getWorld(), pos, Direction.UP, SideShapeType.FULL)) {
                            pos = pos.down();

                            if (!pos.isWithinDistance(getPos(), radius)) {
                                continue blocks;
                            }
                        }
                    }

                    if (pos.isWithinDistance(getPos(), radius)) {
                        getWorld().setBlockState(pos, ModBlocks.GREEK_FIRE.getDefaultState().with(GreekFireBlock.AGE, MathHelper.clamp((int) (strength / 2 + random.nextFloat() * 4 + 2), 0, 15)));
                    }
                }
            }

            discard();
        }
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public float charge() {
        return MathHelper.clamp((20 - age) / 20f, 0, 1);
    }

    @Override
    public void tick() {
        super.tick();

        if (age >= 20) {
            explode();
        } else if (getWorld().isClient) {
            Vec3d pos = getBoundingBox().getBottomCenter().addRandom(random, 0.25f);
            Vec3d vel = getVelocity().multiply(-0.25).addRandom(random, 0.05f);
            getWorld().addParticle(
                    new GreekFireParticleEffect(AttaccaItem.getChargeColor(charge())),
                    true,
                    pos.x - vel.x / 2, pos.y - vel.y / 2, pos.z - vel.z / 2,
                    vel.x, vel.y, vel.z
            );
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Strength", strength);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Strength", NbtElement.FLOAT_TYPE)) {
            strength = nbt.getFloat("Strength");
        }
    }

    @Override
    protected void onCollision(HitResult hit) {
        super.onCollision(hit);
        explode();
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);

        if (getWorld() instanceof ServerWorld server) {
            DamageSource source = getDamageSources().create(DamageTypes.FIREBALL, this, getOwner());
            hit.getEntity().damage(source, MathHelper.clamp(strength / 2, 0, 10));
            EnchantmentHelper.onTargetDamaged(server, hit.getEntity(), source);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}
