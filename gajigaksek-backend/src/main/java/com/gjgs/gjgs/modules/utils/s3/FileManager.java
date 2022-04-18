package com.gjgs.gjgs.modules.utils.s3;

import com.gjgs.gjgs.modules.exception.s3.FileConvertFailedException;
import com.gjgs.gjgs.modules.exception.s3.FileFormatException;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileManager {

    private final AmazonS3Service amazonS3Service;

    public String getUploadedFileName(MultipartFile file, String savePath) {
        String filename = createFileName(file.getOriginalFilename());
        return filename;
    }

    public String getUploadedImageUrl(String savedImagePath, String filename) {
        return amazonS3Service.getFileUrl(savedImagePath, filename);
    }

    public String upload(MultipartFile file, String filename, String savePath) {
        File uploadFile = convert(file)
                .orElseThrow(() -> new FileConvertFailedException());
        String uploadImageUrl = amazonS3Service.uploadFile(uploadFile, filename, savePath);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }


    private Optional<File> convert(MultipartFile file)  {
        File convertFile = new File(file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
            return Optional.empty();
        } catch (IOException e){
            throw new FileConvertFailedException();
        }

    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("저장하려는 File 객체가 삭제되었습니다.");
        } else {
            log.info("저장하려는 File 객체가 삭제되지 못했습니다.");
        }
    }


    private String createFileName(String originalFilename) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(getTodayYYYYMMDD())
                .append("-")
                .append(UUID.randomUUID())
                .append(getFileExtension(originalFilename))
                .toString();
    }

    private String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new FileFormatException();
        }
    }


    private String getTodayYYYYMMDD() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public List<FileInfoVo> uploadFiles(List<MultipartFile> multipartFileList, String savedPath) throws IOException {
        List<FileInfoVo> fileInfoVoList = new ArrayList<>();
        addFileInfo(multipartFileList, savedPath, fileInfoVoList);
        return fileInfoVoList;
    }

    public FileInfoVo uploadFile(MultipartFile file, String savedPath) throws IOException {
        String fileName = getUploadedFileName(file, savedPath);
        String fileUrl = upload(file, fileName, savedPath);
        return FileInfoVo.of(fileName, fileUrl);
    }

    private void addFileInfo(List<MultipartFile> multipartFileList, String savedPath, List<FileInfoVo> fileInfoVoList) throws IOException {
        for (MultipartFile file : multipartFileList) {
            String fileName = getUploadedFileName(file, savedPath);
            String fileUrl = upload(file, fileName, savedPath);
            fileInfoVoList.add(FileInfoVo.of(fileName, fileUrl));
        }
    }
}
