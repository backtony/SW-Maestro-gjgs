package com.gjgs.gjgs.modules.utils.s3.interfaces;

import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SaveDeleteFileManager {

    List<FileInfoVo> deleteAndSaveFiles(FilePaths path, List<String> fileNames, List<MultipartFile> uploadFiles) throws IOException;
}
