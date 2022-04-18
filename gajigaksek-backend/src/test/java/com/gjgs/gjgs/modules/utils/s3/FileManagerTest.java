package com.gjgs.gjgs.modules.utils.s3;

import com.gjgs.gjgs.modules.dummy.FileDummy;
import com.gjgs.gjgs.modules.exception.s3.FileFormatException;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileManagerTest {

    @Mock
    AmazonS3Service amazonS3Service;
    @InjectMocks FileManager fileManager;


    @DisplayName("FileName 추출")
    @Test
    void get_uploaded_file_name() throws Exception {
        //given
        MockMultipartFile file = FileDummy.getFiles();

        //when
        String fileName = fileManager
                .getUploadedFileName(file, "/image/member");

        //then
        assertAll(
                () -> assertTrue(fileName.startsWith(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd")))),
                () -> assertEquals(
                        file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")),
                        fileName.substring(fileName.lastIndexOf("."))
                )
        );
    }

    @DisplayName("FileName 추출 format 에러")
    @Test
    void get_uploaded_file_name_format_error() throws Exception {
        //given
        MockMultipartFile file = FileDummy.getWrongFormatFiles();

        // when then
        assertThrows(FileFormatException.class,
                () -> fileManager.getUploadedFileName(file, "/image/member")
        );
    }


    @DisplayName("upload")
    @Test
    void upload() throws Exception {
        //given
        MockMultipartFile file = FileDummy.getFiles();
        String filename = "test.jpeg";
        String savePath = FilePaths.MEMBER_IMAGE_PATH.name();
        when(amazonS3Service.uploadFile(any(), any(), any())).thenReturn("testUrl");

        //when
        String uploadImageUrl = fileManager.upload(file, filename, savePath);

        //then
        assertEquals("testUrl", uploadImageUrl);
    }

    @DisplayName("multipartFile List 업로드")
    @Test
    void upload_files_test()throws Exception {

        // given
        String filePath = FilePaths.LECTURE_IMAGE_PATH.getPath();
        List<MultipartFile> fileList = List.of(createMockMultipartFile(), createMockMultipartFile());
        String returnUrl = "url";
        when(amazonS3Service.uploadFile(any(), any(), any()))
                .thenReturn(returnUrl);

        // when
        List<FileInfoVo> fileInfoVoList = fileManager.uploadFiles(fileList, filePath);

        // then
        assertAll(
                () -> verify(amazonS3Service, times(2)).uploadFile(any(), any(), any()),
                () -> assertEquals(fileInfoVoList.size(), 2),
                () -> assertThat(fileInfoVoList.stream().map(FileInfoVo::getFileUrl).collect(toList())).contains(returnUrl)
        );
    }

    private MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile("imageFile",
                "imageFile.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "imageFile".getBytes());
    }
}
