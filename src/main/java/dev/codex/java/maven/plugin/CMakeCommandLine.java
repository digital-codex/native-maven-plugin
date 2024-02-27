package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class CMakeCommandLine {
    private final String[] command;
    private Process process;
    private ProcessInputStream inputStream;

    public CMakeCommandLine(String[] command) {
        this.command = command;
    }

    public void execute(Log consumer) throws IOException {
        this.process = Runtime.getRuntime().exec(this.command);
        this.inputStream = new ProcessInputStream(this.process, consumer);
    }

    public Process process() {
        return this.process;
    }

    public ProcessInputStream inputStream() {
        return this.inputStream;
    }

    public String command() {
        return String.join(" ", this.command);
    }

    public static class ProcessInputStream extends Thread {
        private final Process process;
        private final Log consumer;
        private boolean done;

        public ProcessInputStream(Process process, Log consumer) {
            this.process = process;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try(BufferedReader in = this.process.inputReader()) {
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    this.consumer.info("Process " + this.process.pid() + ": " + line);
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