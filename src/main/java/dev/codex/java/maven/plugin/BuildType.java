package dev.codex.java.maven.plugin;

public enum BuildType {
    DEBUG("Debug"), RELEASE("Release");

    private final String value;

    BuildType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}