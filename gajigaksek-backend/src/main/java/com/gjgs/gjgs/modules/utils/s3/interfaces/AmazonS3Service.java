package com.gjgs.gjgs.modules.utils.s3.interfaces;

import java.io.File;

public interface AmazonS3Service {

    String uploadFile(File uploadFile, String filename, String savedPath);

    String getFileUrl(String savedPath, String filename);

    void delete(String savedPath, String filename);
}
