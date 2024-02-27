package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

public class CMakeCommandLineBuilder {
    private int ap = 0;
    private final String executable;
    private String[] args = new String[8];

    public CMakeCommandLineBuilder() throws MojoExecutionException {
        this.executable = ExecutableFinder.findExecutable("cmake");
    }

    public CMakeCommandLineBuilder defineProperty(String key, String value) {
        this.addArgument("-D" + key + "=" + value);
        return this;
    }

    public CMakeCommandLineBuilder addArguments(String... args) {
        for (String arg : args) {
            this.addArgument(arg);
        }
        return this;
    }

    public CMakeCommandLineBuilder addArguments(List<String> args) {
        for (String arg : args) {
            this.addArguments(arg);
        }
        return this;
    }

    public CMakeCommandLine build() {
        String[] command = new String[this.ap + 1];
        command[0] = this.executable;
        System.arraycopy(this.args, 0, command, 1, this.ap);
        return new CMakeCommandLine(command);
    }

    private void addArgument(String arg) {
        if (this.ap + 1 > this.args.length) {
            int oldCapacity = this.args.length;
            String[] newArgs = new String[(oldCapacity < 8) ? 8 : oldCapacity << 1];
            System.arraycopy(this.args, 0, newArgs, 0, oldCapacity);
            this.args = newArgs;
        }

        this.args[this.ap++] = arg;
    }
}