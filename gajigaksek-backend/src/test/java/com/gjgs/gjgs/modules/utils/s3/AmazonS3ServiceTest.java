package com.gjgs.gjgs.modules.utils.s3;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.gjgs.gjgs.infra.config.aws.AmazonS3Config;
import com.gjgs.gjgs.modules.dummy.FileDummy;
import com.gjgs.gjgs.modules.exception.s3.BucketNotFoundException;
import com.gjgs.gjgs.modules.utils.s3.impl.AmazonS3ServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmazonS3ServiceTest {

    @Mock AmazonS3Client amazonS3Client;
    @Mock AmazonS3Config amazonS3Config;
    @InjectMocks
    AmazonS3ServiceImpl amazonS3Service;

    @DisplayName("파일 업로드")
    @Test
    void upload_file() throws Exception {
        //given
        String fileUrl = "https://www.s3.com";
        String fileName = "fineName";
        String savedPath = "testPath";
        MockMultipartFile file = FileDummy.getFiles();
        when(amazonS3Client.getUrl(any(), any())).thenReturn(new URL(fileUrl));

        //when
        String savedUrl = amazonS3Service.uploadFile(new File(file.getOriginalFilename()), fileName, savedPath);

        //then
        assertEquals(fileUrl, savedUrl);
    }

    @DisplayName("파일 업로드 버킷 에러")
    @Test
    void upload_file_bucket_error() throws Exception {
        //given
        String fileName = "fineName";
        String savedPath = "testPath";
        MockMultipartFile file = FileDummy.getFiles();
        when(amazonS3Client.putObject(any())).thenThrow(AmazonS3Exception.class);

        //when then
        assertThrows(BucketNotFoundException.class,
                () -> amazonS3Service.uploadFile(new File(file.getOriginalFilename()), fileName, savedPath));
    }

    @DisplayName("저장된 파일 url 가져오기")
    @Test
    void get_file_url() throws Exception {
        //given
        String fileUrl = "https://www.s3.com";
        String fileName = "fineName";
        String savedPath = "testPath";
        when(amazonS3Client.getUrl(any(), any())).thenReturn(new URL(fileUrl));

        //when
        String savedFileUrl = amazonS3Service.getFileUrl(savedPath, fileName);

        //then
        assertEquals(fileUrl, savedFileUrl);
    }

    @DisplayName("저장된 파일 url 가져오기 버킷 에러")
    @Test
    void get_file_url_bucket_error() throws Exception {
        //given
        String fileUrl = "https://www.s3.com";
        String fileName = "fineName";
        String savedPath = "testPath";
        when(amazonS3Client.getUrl(any(), any())).thenThrow(AmazonS3Exception.class);

        // when then
        assertThrows(BucketNotFoundException.class,
                () -> amazonS3Service.getFileUrl(savedPath, fileName));
    }

}
