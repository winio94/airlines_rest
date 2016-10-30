package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.lines;

public final class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static List<String> readAllLinesFrom(String path, String splitRegex) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        Path p = null;
        try {
            p = Paths.get(absolutePath);
            lines = lines(p)
                    .map(line -> line.split(splitRegex))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList());
        } catch (NoSuchFileException e) {
            LOGGER.error("Could not find file for given path: {}.", p);
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("Could not open file for given path: {}.", p);
            e.printStackTrace();
        }
        return lines;
    }
}