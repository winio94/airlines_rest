package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
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
        ClassPathResource classPathResource = null;
        try {
            classPathResource = new ClassPathResource(path);
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            lines = bufferedReader.lines()
                    .map(line -> line.split(splitRegex))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList());
        } catch (NoSuchFileException e) {
            LOGGER.error("Could not find file for given path: {}.", classPathResource.getPath());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("Could not open file for given path: {}.", classPathResource.getPath());
            e.printStackTrace();
        }
        return lines;
    }
}