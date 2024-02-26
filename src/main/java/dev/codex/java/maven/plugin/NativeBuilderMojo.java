package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "build",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = true
)
public class NativeBuilderMojo extends AbstractNativeMojo {
    private final Log LOGGER = this.getLog();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        LOGGER.info("Building project");
        super.execute();
    }
}