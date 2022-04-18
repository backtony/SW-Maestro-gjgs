package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.exception.lecture.MissingFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class AbstractFileCheck {

    protected void fileCheck(List<MultipartFile> files, int targetSize) {
        fileIsMissing(files);
        checkFileSize(files, targetSize);
    }

    private void fileIsMissing(List<MultipartFile> files) {
        if (files.size() == 1 && files.get(0).getOriginalFilename().isBlank()) {

            throw new MissingFileException();
        }
    }

    protected abstract void checkFileSize(List<MultipartFile> files, int targetSize);
}
