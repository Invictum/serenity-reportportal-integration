package com.github.invictum.reportportal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides access to shared sync directory. Required for launches merge functionality
 */
public class FileStorage {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorage.class);

    /**
     * Valid file name pattern
     */
    private static final Predicate<Path> VALID = path -> path.getFileName().toString().matches("^\\d+$");

    private Path root;

    /**
     * Inits storage in specific location
     * If folder is absent it will be created. Warning: at the end of storage life cycle it will be removed
     *
     * @param root path for storage
     */
    public FileStorage(String root) {
        this.root = Paths.get(root);
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            LOG.warn("Path at {} is not writable. Merge may fail", root);
        }
    }

    /**
     * Creates file in a storage
     *
     * @param id of file to create, usually it is launch ID
     */
    public void touch(Long id) {
        Path path = root.resolve(id.toString());
        try {
            Files.createFile(path);
        } catch (IOException e) {
            LOG.warn("Path at {} is not writable. Merge may fail", root);
        }
    }

    /**
     * Checks the count of launch files inside storage
     */
    public long count() {
        try {
            return Files.list(root).filter(VALID).count();
        } catch (IOException e) {
            LOG.warn("Path at {} is not readable. Merge may fail", root);
            return 0;
        }
    }

    /**
     * Collects the set of launches and removes storage
     */
    public Set<Long> loadAndClean() {
        Set<Long> ids = new HashSet<>();
        try {
            Files.list(root).filter(VALID).forEach(path -> {
                ids.add(Long.parseLong(path.getFileName().toString()));
                secureRemove(path);
            });
            Files.delete(root);
        } catch (IOException e) {
            LOG.warn("Path at {} is not writable. Merge mechanism may fail", root);
        }
        return ids;
    }

    private static void secureRemove(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            LOG.debug("Unable to remove file at {}", path);
        }
    }
}