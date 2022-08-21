package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final UberMapper mapper;

    @Value("${app.book.file_path}")
    private String path;

    public BookFile createBookFile(InputStream inputStream, String filename, String mimeType, int contentLength) throws IOException {

        var name = UUID.randomUUID().toString();
        File targetFile = new File(path + name);
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[contentLength * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        var bookFile = BookFileEntity.builder()
                .name(name)
                .fileExtension(filename.split("\\.")[1])
                .build();

        return mapper.map(bookFile);
    }
}
