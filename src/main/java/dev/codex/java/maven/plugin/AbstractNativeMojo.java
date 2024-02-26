package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractNativeMojo extends AbstractMojo {
    protected static final Toolchain DEFAULT_TOOLCHAIN = new Toolchain();

    private final Log LOGGER = this.getLog();

    @Parameter(property = "native.build.type", defaultValue = "DEBUG", readonly = true)
    private BuildType buildType;

    @Parameter
    private Toolchain toolchain;

    @Parameter(property = "native.build.generator", defaultValue = "NINJA", required = true, readonly = true)
    private Generator generator;

    @Parameter(property = "native.source.path", defaultValue = "${basedir}", required = true, readonly = true)
    private String source;

    @Parameter(property = "native.build.path", defaultValue = "${project.build.directory}", required = true, readonly = true)
    private String build;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Toolchain toolchain = (this.toolchain != null)
                ? this.toolchain : DEFAULT_TOOLCHAIN;

        LOGGER.info(toolchain.toString());
    }
}