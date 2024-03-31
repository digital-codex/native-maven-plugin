package dev.codex.java.maven.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

// TODO: integration test Mojos
@Mojo(
        name = "build",
        defaultPhase = LifecyclePhase.COMPILE
)
public class NativeBuilderMojo extends AbstractNativeMojo {
    private static final String BUILD = "--build";
    private static final String TARGET = "--target";

    @Override
    public CMakeCommandLine command() {
        return new CMakeCommandLineBuilder()
                .add(NativeBuilderMojo.BUILD, this.buildDirectory)
                .add(NativeBuilderMojo.TARGET, this.target)
                .add(this.options)
                .build();
    }
}