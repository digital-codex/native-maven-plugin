package dev.codex.java.maven.plugin;

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
    public CMakeCommandLine command() {
        return new CMakeCommandLineBuilder()
                .add("-D" + NativeGeneratorMojo.CMAKE_BUILD_TYPE + "=" + this.buildType.value())
                .add("-D" + NativeGeneratorMojo.CMAKE_MAKE_PROGRAM + "=" + ExecutableFinder.findExecutable(this.toolchain.generator()))
                .add("-D" + NativeGeneratorMojo.CMAKE_C_COMPILER + "=" + ExecutableFinder.findExecutable(this.toolchain.ccompiler()))
                .add("-D" + NativeGeneratorMojo.CMAKE_CXX_COMPILER + "=" + ExecutableFinder.findExecutable(this.toolchain.cxxcompiler()))
                .add("-G", Generator.valueOf(this.toolchain.generator().toUpperCase()).name())
                .add("-S", this.sourceDirectory)
                .add("-B", this.buildDirectory)
                .build();
    }
}