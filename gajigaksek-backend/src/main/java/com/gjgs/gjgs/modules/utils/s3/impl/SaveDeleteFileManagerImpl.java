package com.gjgs.gjgs.modules.utils.s3.impl;

import com.gjgs.gjgs.modules.utils.s3.FileManager;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.s3.interfaces.SaveDeleteFileManager;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveDeleteFileManagerImpl implements SaveDeleteFileManager {

    private final FileManager fileManager;
    private final AmazonS3Service s3Service;

    @Override
    public List<FileInfoVo> deleteAndSaveFiles(FilePaths filePath, List<String> deleteFilenames, List<MultipartFile> uploadFiles) throws IOException {
        String path = filePath.getPath();

        deleteFilenames.forEach(fileName ->
                s3Service.delete(path, fileName));

        return fileManager.uploadFiles(uploadFiles, path);
    }
}
