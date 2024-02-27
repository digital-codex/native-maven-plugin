package dev.codex.java.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ExecutableFinder {
    private static final String PATH = System.getenv("PATH");
    private static final Map<String, String> CACHE = new HashMap<>();

    private ExecutableFinder() {
        super();
    }

    // TODO: validate path is not null
    public static String findExecutable(String target) throws MojoExecutionException {
        String found = ExecutableFinder.CACHE.get(target);
        if (found != null)
            return found;

        Queue<String> queue = new ArrayDeque<>(
                Arrays.asList(ExecutableFinder.PATH.split(":"))
        );

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current == null)
                continue;

            File file = new File(current);
            if (!file.exists())
                continue;

            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children == null)
                    continue;

                for (File child : children)
                    queue.add(child.getPath());
            } else {
                if (CACHE.get(file.getName()) != null)
                    continue;

                CACHE.put(file.getName(), file.getPath());
                if (file.getName().compareTo(target) == 0)
                    return file.getPath();
            }
        }

        throw new MojoExecutionException("No such executable '" + target + "'.");
    }
}