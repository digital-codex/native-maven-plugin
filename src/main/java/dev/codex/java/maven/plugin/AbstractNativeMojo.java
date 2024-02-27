package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.List;

public abstract class AbstractNativeMojo extends AbstractMojo {
    public enum ExecutionType {
        GENERATE_BUILD_SYSTEM,
        BUILD_PROJECT
    }
    protected static final Toolchain DEFAULT_TOOLCHAIN = new Toolchain();

    protected CMakeCommandLine command;

    private static final String CMAKE_BUILD_TYPE = "CMAKE_BUILD_TYPE";
    private static final String CMAKE_MAKE_PROGRAM = "CMAKE_MAKE_PROGRAM";
    private static final String CMAKE_C_COMPILER = "CMAKE_C_COMPILER";
    private static final String CMAKE_CXX_COMPILER = "CMAKE_CXX_COMPILER";

    private static final String BUILD = "--build";
    private static final String TARGET = "--target";

    @Parameter(property = "native.build.type", defaultValue = "DEBUG")
    private BuildType buildType;

    @Parameter
    private Toolchain toolchain;

    @Parameter(property = "native.build.generator", defaultValue = "NINJA", required = true)
    private Generator generator;

    @Parameter(property = "native.source.path", defaultValue = "${basedir}", required = true, readonly = true)
    private String sourceDirectory;

    @Parameter(property = "native.build.path", defaultValue = "${project.build.directory}", required = true, readonly = true)
    private String buildDirectory;

    @Parameter(property = "native.build.target", required = true)
    private String target;

    @Parameter(property = "native.build.options")
    private List<String> options;

    public void execute(ExecutionType type) throws MojoExecutionException, MojoFailureException, IOException, InterruptedException {
        Toolchain toolchain = (this.toolchain != null)
                ? this.toolchain : DEFAULT_TOOLCHAIN;

        this.command = switch (type) {
            case GENERATE_BUILD_SYSTEM ->
                    new CMakeCommandLineBuilder()
                            .defineProperty(AbstractNativeMojo.CMAKE_BUILD_TYPE, this.buildType.value())
                            .defineProperty(AbstractNativeMojo.CMAKE_MAKE_PROGRAM, ExecutableFinder.findExecutable(toolchain.generator()))
                            .defineProperty(AbstractNativeMojo.CMAKE_C_COMPILER, ExecutableFinder.findExecutable(toolchain.ccompiler()))
                            .defineProperty(AbstractNativeMojo.CMAKE_CXX_COMPILER, ExecutableFinder.findExecutable(toolchain.cxxcompiler()))
                            .addArguments("-G", this.generator.value())
                            .addArguments("-S", this.sourceDirectory)
                            .addArguments("-B", this.buildDirectory)
                            .build();
            case BUILD_PROJECT -> {
                CMakeCommandLineBuilder builder = new CMakeCommandLineBuilder()
                        .addArguments(AbstractNativeMojo.BUILD, this.buildDirectory)
                        .addArguments(AbstractNativeMojo.TARGET, this.target);
                for (String option : this.options) {
                    builder.addArguments(option);
                }
                yield builder.build();
            }
        };

        int returnValue;
        this.getLog().info("Executing: " + String.join(" ", this.command.command()));

        this.command.execute(this.getLog());
        this.command.outputStream().start();

        returnValue = this.command.process().waitFor();

        try {
            this.command.outputStream().waitFor();
        } catch (InterruptedException e) {
            throw new MojoExecutionException(e);
        }

        if (returnValue != 0)
            throw new MojoFailureException("Process had nonzero return value: returned " + returnValue);
    }
}