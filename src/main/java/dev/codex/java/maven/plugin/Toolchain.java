package dev.codex.java.maven.plugin;

import java.util.Objects;

public class Toolchain {
    private final String generator;
    private final String ccompiler;
    private final String cxxcompiler;

    public Toolchain() {
        this("ninja", "clang", "clang++");
    }

    public Toolchain(String generator, String ccompiler, String cxxcompiler) {
        this.generator = generator;
        this.ccompiler = ccompiler;
        this.cxxcompiler = cxxcompiler;
    }

    public String generator() {
        return this.generator;
    }

    public String ccompiler() {
        return this.ccompiler;
    }

    public String cxxcompiler() {
        return this.cxxcompiler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Toolchain that)) return false;
        return Objects.equals(this.generator, that.generator)
                && Objects.equals(this.ccompiler, that.ccompiler)
                && Objects.equals(this.cxxcompiler, that.cxxcompiler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.generator, this.ccompiler, this.cxxcompiler);
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