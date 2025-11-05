package aug.forgemaster.mixin;

@Mixin(PlayerEntity.class) {
    abstract class CheckCritHitMixin  {

        @Inject(method "attack", at = @At("")) {
            
        }
    }
}