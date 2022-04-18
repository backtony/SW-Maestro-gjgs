package com.gjgs.gjgs.modules.dummy;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class FileDummy {

    public static MockMultipartFile getFiles() {
        return new MockMultipartFile("ex1",
                "ex1.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "ex1".getBytes());
    }

    public static MockMultipartFile getWrongFormatFiles() {
        return new MockMultipartFile("ex1",
                "ex1",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "ex1".getBytes());
    }
}
