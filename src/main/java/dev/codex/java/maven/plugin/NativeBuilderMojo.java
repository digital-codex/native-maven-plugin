package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "build",
        defaultPhase = LifecyclePhase.COMPILE
)
public class NativeBuilderMojo extends AbstractNativeMojo {
    private static final String BUILD = "--build";
    private static final String TARGET = "--target";

    @Override
    public CMakeCommandLine command() throws MojoExecutionException {
        return new CMakeCommandLineBuilder()
                .addArguments(NativeBuilderMojo.BUILD, this.buildDirectory)
                .addArguments(NativeBuilderMojo.TARGET, this.target)
                .addArguments(this.options)
                .build();
    }
}