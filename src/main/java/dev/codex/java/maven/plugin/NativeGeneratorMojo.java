package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.COMPILE
)
public class NativeGeneratorMojo extends AbstractNativeMojo {
    private static final String CMAKE_BUILD_TYPE = "CMAKE_BUILD_TYPE";
    private static final String CMAKE_MAKE_PROGRAM = "CMAKE_MAKE_PROGRAM";
    private static final String CMAKE_C_COMPILER = "CMAKE_C_COMPILER";
    private static final String CMAKE_CXX_COMPILER = "CMAKE_CXX_COMPILER";

    @Override
    public CMakeCommandLine command() throws MojoExecutionException {
        return new CMakeCommandLineBuilder()
                .defineProperty(NativeGeneratorMojo.CMAKE_BUILD_TYPE, this.buildType.value())
                .defineProperty(NativeGeneratorMojo.CMAKE_MAKE_PROGRAM, ExecutableFinder.findExecutable(this.toolchain.generator()))
                .defineProperty(NativeGeneratorMojo.CMAKE_C_COMPILER, ExecutableFinder.findExecutable(this.toolchain.ccompiler()))
                .defineProperty(NativeGeneratorMojo.CMAKE_CXX_COMPILER, ExecutableFinder.findExecutable(this.toolchain.cxxcompiler()))
                .addArguments("-G", this.generator.value())
                .addArguments("-S", this.sourceDirectory)
                .addArguments("-B", this.buildDirectory)
                .build();
    }
}