package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoFailureException;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public record Toolchain(String generator, String ccompiler, String cxxcompiler) {
    public String[] findTools() throws MojoFailureException {
        String[] candidates = System.getenv("PATH").split(":");
        List<Path> searchPaths = new ArrayList<>();
        for (String dir : candidates) {
            try {
                searchPaths.add(Paths.get(dir));
            } catch (InvalidPathException e) {
                // continue
            }
        }
        return new String[3];
    }

    @Override
    public String toString() {
        return "Toolchain{" +
                "build tool: " + this.generator +
                ", c compiler: " + this.ccompiler +
                ", c++ compiler: " + this.cxxcompiler +
                "}";
    }
}