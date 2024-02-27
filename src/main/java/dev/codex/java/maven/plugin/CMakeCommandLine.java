package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

public class CMakeCommandLine {
    private int ap = 0;
    private final String executable;
    private String[] args = new String[8];

    public CMakeCommandLine() throws MojoExecutionException {
        this.executable = ExecutableFinder.findExecutable("cmake");
    }

    public CMakeCommandLine defineProperty(String key, String value) {
        this.addArgument("-D" + key + "=" + value);
        return this;
    }

    public CMakeCommandLine addArguments(String... args) {
        for (String arg : args) {
            this.addArgument(arg);
        }
        return this;
    }

    public Process execute() throws IOException {
        return Runtime.getRuntime().exec(this.makeCommand());
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

    public String[] makeCommand() {
        String[] command = new String[this.ap + 1];
        command[0] = this.executable;
        System.arraycopy(this.args, 0, command, 1, this.ap);
        return command;
    }
}