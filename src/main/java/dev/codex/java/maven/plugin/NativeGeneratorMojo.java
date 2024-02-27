package dev.codex.java.maven.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.COMPILE
)
public class NativeGeneratorMojo extends AbstractNativeMojo {
    @Override
    public ExecutionGoal getGoal() {
        return ExecutionGoal.GENERATE;
    }
}