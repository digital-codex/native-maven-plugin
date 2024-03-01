package dev.codex.java.maven.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "build",
        defaultPhase = LifecyclePhase.COMPILE
)
public class NativeBuilderMojo extends AbstractNativeMojo {
    @Override
    public ExecutionGoal goal() {
        return ExecutionGoal.BUILD;
    }
}