package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.List;

public abstract class AbstractNativeMojo extends AbstractMojo {
    @Parameter(property = "native.build.type", defaultValue = "DEBUG")
    protected BuildType buildType;

    @Parameter
    protected final Toolchain toolchain = new Toolchain();

    @Parameter(property = "native.build.generator", defaultValue = "NINJA", required = true)
    protected Generator generator;

    @Parameter(property = "native.source.path", defaultValue = "${basedir}", required = true, readonly = true)
    protected String sourceDirectory;

    @Parameter(property = "native.build.path", defaultValue = "${project.build.directory}", required = true, readonly = true)
    protected String buildDirectory;

    @Parameter(property = "native.build.target", required = true)
    protected String target;

    @Parameter(property = "native.build.options")
    protected List<String> options;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        CMakeCommandLine command = this.command();

        int returnValue;
        this.getLog().info("Executing: " + String.join(" ", command.command()));

        try {
            command.execute(this.getLog());
        } catch (IOException e) {
            throw new MojoExecutionException(e);
        }
        command.outputStream().start();

        try {
            returnValue = command.process().waitFor();

            command.outputStream().waitFor();
        } catch (InterruptedException e) {
            throw new MojoExecutionException(e);
        }

        if (returnValue != 0)
            throw new MojoFailureException("Process had nonzero return value: returned " + returnValue);
    }

    public abstract CMakeCommandLine command() throws MojoExecutionException;
}