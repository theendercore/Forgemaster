package aug.forgemaster.mixin;

@Mixin(MinecraftServer.class) {
    abstract class TutorialMixin extends ReentrantThreadExecutor<ServerTask> implements QueryableServer, ChunkErrorHandler, CommandOutput, AutoCloseable {

        public TutorialMixin(String string) {
            super(string);
        }

        @Inject(method = "loadWorld", at = @At("HEAD"))
        private void logOnWorldLoad(CallbackInfo ci) {
            Forgemaster.LOGGER.info("MinecraftServer$loadWorld has started!")

        }
    }
}