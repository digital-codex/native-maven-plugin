package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class CMakeCommandLine {
    private final String[] command;
    private Process process;
    private ProcessOutputStream outputStream;

    public CMakeCommandLine(String[] command) {
        this.command = command;
    }

    public void execute(Log consumer) throws IOException {
        this.process = Runtime.getRuntime().exec(this.command);
        this.outputStream = new ProcessOutputStream(this.process, consumer);
    }

    public Process process() {
        return this.process;
    }

    public ProcessOutputStream outputStream() {
        return this.outputStream;
    }

    public String command() {
        return String.join(" ", this.command);
    }

    public static class ProcessOutputStream extends Thread {
        private final Process process;
        private final Log consumer;
        private boolean done;

        public ProcessOutputStream(Process process, Log consumer) {
            this.process = process;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try(BufferedReader in = this.process.inputReader()) {
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    this.consumer.debug("Process " + this.process.pid() + ": " + line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.done = true;
        }

        public synchronized void waitFor() throws InterruptedException {
            while (!this.done) {
                this.wait();
            }
        }
    }
}