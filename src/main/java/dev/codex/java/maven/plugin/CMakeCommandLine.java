package dev.codex.java.maven.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class CMakeCommandLine {
    @FunctionalInterface
    public interface ProcessConsumer {
        void read(String data);
    }

    @FunctionalInterface
    public interface ProcessSupplier {
        String write();
    }

    private final String[] command;
    private Process process;
    private ProcessOutputStream output;
    private ProcessInputStream input;

    public CMakeCommandLine(String[] command) {
        this.command = command;
    }

    public void execute() throws IOException {
        this.execute(null, null);
    }

    public void execute(ProcessConsumer consumer) throws IOException {
        this.execute(consumer, null);
    }

    public void execute(ProcessSupplier supplier) throws IOException {
        this.execute(null, supplier);
    }

    public void execute(ProcessConsumer consumer, ProcessSupplier supplier) throws IOException {
        this.process = Runtime.getRuntime().exec(this.command);

        if (consumer != null) {
            this.output = new ProcessOutputStream(this.process, consumer);
        }

        if (supplier != null) {
            this.input = new ProcessInputStream(this.process, supplier);
        }
    }

    public synchronized int waitFor() throws InterruptedException {
        int ret  = this.process.waitFor();

        if (this.output != null) {
            this.output.waitFor();
        }

        if (this.input != null) {
            this.input.waitFor();
        }

        return ret;
    }

    public ProcessOutputStream output() {
        return this.output;
    }

    // TODO: test input/output streams
    public ProcessInputStream input() {
        return this.input;
    }

    public static class ProcessOutputStream extends Thread {
        private final Process process;
        private final ProcessConsumer consumer;
        private boolean done;

        public ProcessOutputStream(Process process, ProcessConsumer consumer) {
            this.process = process;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try(BufferedReader in = this.process.inputReader()) {
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    this.consumer.read("Process " + this.process.pid() + ": " + line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            synchronized (this) {
                this.done = true;
                this.notifyAll();
            }
        }

        public synchronized void waitFor() throws InterruptedException {
            while (!this.done) {
                this.wait();
            }
        }
    }

    public static class ProcessInputStream extends Thread {
        private final Process process;
        private final ProcessSupplier supplier;
        private boolean done;

        public ProcessInputStream(Process process, ProcessSupplier supplier) {
            this.process = process;
            this.supplier = supplier;
        }

        @Override
        public void run() {
            try(BufferedWriter out = this.process.outputWriter()) {
                for (String line = this.supplier.write(); line != null; line = this.supplier.write()) {
                    out.write(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            synchronized (this) {
                this.done = true;
                this.notifyAll();
            }
        }

        public synchronized void waitFor() throws InterruptedException {
            while (!this.done) {
                this.wait();
            }
        }
    }

    @Override
    public String toString() {
        return String.join(" ", this.command);
    }
}