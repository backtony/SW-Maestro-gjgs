package com.gjgs.gjgs.document.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gjgs.gjgs.config.SecuritySupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@Disabled
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
public abstract class RestDocsTestSupport extends SecuritySupport {

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected RestDocumentationResultHandler restDocs;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(final WebApplicationContext context,
               final RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(springSecurity())
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    protected List<FieldDescriptor> sliceDescriptor() {
        return new ArrayList<>(List.of(
                fieldWithPath("pageable.sort.sorted").type(BOOLEAN).description("정렬 여부"),
                fieldWithPath("pageable.sort.unsorted").type(BOOLEAN).description("정렬 안하는지 여부"),
                fieldWithPath("pageable.sort.empty").type(BOOLEAN).description("정렬 조건이 아무것도 없는지의 여부"),
                fieldWithPath("pageable.offset").type(NUMBER).description("현재 페이지"),
                fieldWithPath("pageable.pageNumber").type(NUMBER).description("현재 페이지 번호"),
                fieldWithPath("pageable.pageSize").type(NUMBER).description("한 페이지에 보여줄 컨텐츠 크기"),
                fieldWithPath("pageable.paged").type(BOOLEAN).description("페이징 되었는지 여부"),
                fieldWithPath("pageable.unpaged").type(BOOLEAN).description("페이징 안되었는지 여부"),
                fieldWithPath("empty").type(BOOLEAN).description("조회가 아무것도 되지 않았는지 여부"),
                fieldWithPath("last").type(BOOLEAN).description("마지막 인지의 여부"),
                fieldWithPath("number").type(NUMBER).description("페이지"),
                fieldWithPath("size").type(NUMBER).description("한 페이지의 크기"),
                fieldWithPath("numberOfElements").type(NUMBER).description("이 페이지에서 검색된 크기(size와 다른 개념)"),
                fieldWithPath("first").type(BOOLEAN).description("첫 페이지인지의 여부"),
                fieldWithPath("sort.sorted").type(BOOLEAN).description("정렬 여부"),
                fieldWithPath("sort.unsorted").type(BOOLEAN).description("정렬 안하는지 여부"),
                fieldWithPath("sort.empty").type(BOOLEAN).description("정렬 조건이 아무것도 없는지의 여부")
        ));
    }

    protected List<FieldDescriptor> pageDescriptor() {
        List<FieldDescriptor> fieldDescriptors = sliceDescriptor();
        fieldDescriptors.add(fieldWithPath("totalPages").type(NUMBER).description("컨텐츠의 총 페이지 수"));
        fieldDescriptors.add(fieldWithPath("totalElements").type(NUMBER).description("총 컨텐츠 수"));
        return fieldDescriptors;
    }


    protected List<FieldDescriptor> errorDescriptor() {
        return new ArrayList<>(List.of(
                fieldWithPath("message").description("에러 메시지"),
                fieldWithPath("code").description("에러 코드"),
                fieldWithPath("errors").description("Error 값 배열 값"),
                fieldWithPath("errors[0].field").description("에러 필드명"),
                fieldWithPath("errors[0].value").description("에러 필드값"),
                fieldWithPath("errors[0].reason").description("에러 이유")
        ));
    }
}