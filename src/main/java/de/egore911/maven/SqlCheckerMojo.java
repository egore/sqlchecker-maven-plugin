package de.egore911.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Christoph Brill &lt;egore911@gmail.com&gt;
 * @since 27.05.15 20:55
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.COMPILE)
@Execute(goal = "check")
public class SqlCheckerMojo extends AbstractMojo {

    @Parameter(property = "check.directories")
    private String[] directories;

    @Parameter(property = "check.fatal", defaultValue = "true")
    private boolean fatal;

    public void execute() throws MojoExecutionException {
        if (directories == null || directories.length < 2) {
            throw new MojoExecutionException("You must specify at least two directories for comparison");
        }

        Map<String, Set<String>> scriptFiles = new HashMap<>(directories.length);

        for (String directory : directories) {
            Path path = Paths.get(directory);
            if (!Files.exists(path)) {
                throw new MojoExecutionException("The directory '" + directory + "' does not exist");
            }
            if (!Files.isDirectory(path)) {
                throw new MojoExecutionException("The path '" + directory + "' is not a directory");
            }

            Set<String> scriptFilesInDirectory = new HashSet<>();
            scriptFiles.put(directory, scriptFilesInDirectory);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
                for (Path file : directoryStream) {
                    scriptFilesInDirectory.add(file.getFileName().toString());
                }
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }

        for (Map.Entry<String, Set<String>> outer : scriptFiles.entrySet()) {
            for (Map.Entry<String, Set<String>> inner : scriptFiles.entrySet()) {
                if (outer.equals(inner)) {
                    continue;
                }
                if (!inner.getValue().containsAll(outer.getValue())) {
                    outer.getValue().removeAll(inner.getValue());
                    String message = "Incomplete directory found\n" +
                            inner.getKey() + " is lacking " + outer.getValue() + " available in " + outer.getKey();
                    if (fatal) {
                        throw new MojoExecutionException(message);
                    } else {
                        getLog().warn(message);
                    }
                }
            }
        }
    }

}
