package ru.gpb.urisad.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gpb.urisad.config.MailParserProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MailDownloader {
    private List<Integer> positions = new ArrayList<>();
    private boolean isFileContractCorrect;

    public void extract(MailParserProperties properties) {
        final String targetStrings = properties.getTargetStrings();
        final String filteredStrings = properties.getFilteredStrings();

        try {
            for (String file : listFilesUsingDirectoryStream(properties.getTargetDir())) {
                isFileContractCorrect = false;
                extracted(properties.getTargetDir().concat(file),
                        targetStrings,
                        filteredStrings,
                        properties.getSplitChar());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extracted(String fl, String targetStrings, String filteredStrings, String separator) {
        try {
            String line;
            File file = new File(fl);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(targetStrings)) {
                    isFileContractCorrect = true;
                }
                if (!isFileContractCorrect) continue;
                else {
                    log.info("File Contract Checked with status: true");
                    List<String> targetStringsParsed = List.of(targetStrings.split(separator));
                    List<String> filteredStringsParsed = List.of(filteredStrings.split(separator));
                    targetStringsParsed.forEach(s -> {
                        if (filteredStringsParsed.contains(s)) {
                            positions.add(targetStringsParsed.indexOf(s));
                        }
                    });
                    line = reader.readLine();
                    parseLine(line, positions, separator);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!isFileContractCorrect) log.error("File Contract Checked with status: false");
    }

    private void parseLine(String line, List<Integer> positions, String splitChar) {
        List<String> parsedLine = Arrays.stream(line.split(splitChar))
                .distinct()
                .collect(Collectors.toList());
        parsedLine.forEach(s -> {
            if (positions.contains(parsedLine.indexOf(s))) {
                System.out.println(s);
            }
        });
    }

    public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileList;
    }
}
