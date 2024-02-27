package dev.codex.java.maven.plugin;

public enum Generator {
    NINJA("Ninja");

    private final String value;

    Generator(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}