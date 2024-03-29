package dev.codex.java.maven.plugin;

import dev.codex.java.io.file.FileTreeWalker;

public class CMakeCommandLineBuilder {
    private record Argument(String... parts) {}

    private final String executable;
    private Argument[] arguments = new Argument[8];
    private int count = 0;

    public CMakeCommandLineBuilder() {
        this.executable = FileTreeWalker.find("cmake").get().toString();
    }

    public CMakeCommandLineBuilder add(String... args) {
        this.add(new Argument(args));
        return this;
    }

    public CMakeCommandLine build() {
        return new CMakeCommandLine(this.command());
    }

    private void add(Argument arg) {
        if (this.count + 1 > this.arguments.length) {
            int oldCapacity = this.arguments.length;
            Argument[] newArguments = new Argument[oldCapacity << 1];
            System.arraycopy(this.arguments, 0, newArguments, 0, oldCapacity);
            this.arguments = newArguments;
        }
        this.arguments[this.count++] = arg;
    }

    private String[] command() {
        String[] args = new String[this.arguments.length * 2];
        args[0] = this.executable; int count = 1;

        for (Argument arg : this.arguments) {
            if (arg == null)
                continue;

            for (String part : arg.parts()) {
                if (count + 1 > args.length) {
                    int oldCapacity = args.length;
                    String[] newArgs = new String[(oldCapacity < 8) ? 8 : oldCapacity << 1];
                    System.arraycopy(args, 0, newArgs, 0, oldCapacity);
                    args = newArgs;
                }
                args[count++] = part;
            }
        }

        String[] newArgs = new String[count];
        System.arraycopy(args, 0, newArgs, 0, count);
        return newArgs;
    }
}