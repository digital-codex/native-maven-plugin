package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = true
)
public class NativeGeneratorMojo extends AbstractNativeMojo {
    private final Log LOGGER = this.getLog();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        LOGGER.info("Generating build system");
        super.execute();
    }
}