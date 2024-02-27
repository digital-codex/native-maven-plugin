package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;

@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = true
)
public class NativeGeneratorMojo extends AbstractNativeMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            super.execute(ExecutionType.GENERATE_BUILD_SYSTEM);
        } catch (IOException e) {
            throw new MojoExecutionException(e);
        }
    }
}