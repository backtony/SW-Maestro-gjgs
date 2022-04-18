package com.gjgs.gjgs.modules.utils.s3.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gjgs.gjgs.infra.config.aws.AmazonS3Config;
import com.gjgs.gjgs.modules.exception.s3.BucketNotFoundException;
import com.gjgs.gjgs.modules.exception.s3.FileDeleteFailedException;
import com.gjgs.gjgs.modules.exception.s3.GetUrlFailedException;
import com.gjgs.gjgs.modules.exception.s3.UploadFailException;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final AmazonS3Client amazonS3Client;
    private final AmazonS3Config amazonS3Config;

    @Override
    public String uploadFile(File uploadFile, String filename, String savedPath) {

        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    amazonS3Config.getBucket() + savedPath,
                    filename,
                    uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 권한설정
            return getFileUrl(savedPath, filename);
        } catch (AmazonS3Exception e) {
            throw new BucketNotFoundException();
        } catch (Exception e) {
            throw new UploadFailException();
        }

    }

    @Override
    public String getFileUrl(String savedPath, String filename) {
        try {
            return amazonS3Client
                    .getUrl(amazonS3Config.getBucket() + savedPath, filename)
                    .toString();
        } catch (AmazonS3Exception e) {
            throw new BucketNotFoundException();
        } catch (Exception e) {
            throw new GetUrlFailedException();
        }

    }

    @Override
    public void delete(String savedPath, String filename) {
        try {
            amazonS3Client.deleteObject(amazonS3Config.getBucket() + savedPath, filename);
        } catch (AmazonS3Exception e) {
            throw new BucketNotFoundException();
        } catch (Exception e) {
            throw new FileDeleteFailedException();
        }
    }


}
