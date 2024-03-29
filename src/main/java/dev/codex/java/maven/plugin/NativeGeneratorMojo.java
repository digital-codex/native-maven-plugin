package dev.codex.java.maven.plugin;

import dev.codex.java.io.file.FileTreeWalker;
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
                .add(AbstractNativeMojo.define(NativeGeneratorMojo.CMAKE_BUILD_TYPE, this.buildType.value()))
                .add(AbstractNativeMojo.define(NativeGeneratorMojo.CMAKE_MAKE_PROGRAM, FileTreeWalker.find(this.toolchain.generator()).get().toString()))
                .add(AbstractNativeMojo.define(NativeGeneratorMojo.CMAKE_C_COMPILER, FileTreeWalker.find(this.toolchain.ccompiler()).get().toString()))
                .add(AbstractNativeMojo.define(NativeGeneratorMojo.CMAKE_CXX_COMPILER, FileTreeWalker.find(this.toolchain.cxxcompiler()).get().toString()))
                .add("-G", Generator.valueOf(this.toolchain.generator().toUpperCase()).value())
                .add("-S", this.sourceDirectory)
                .add("-B", this.buildDirectory)
                .build();
    }
}