package com.yeoboya.lunch.api.docs;

import com.yeoboya.lunch.api.container.ContainerDI;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
class MemberControllerDocTest extends ContainerDI {

    @Test
    void member() throws Exception {

        mockMvc.perform(get("/member"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member/get/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("?????????").optional(),
                                parameterWithName("size").description("?????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.[].email").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].name").description("??????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("5")),
                                fieldWithPath("data.[].bankName").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .optional(),
                                fieldWithPath("data.[]accountNumber").description("????????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional(),
                                fieldWithPath("data.[]nickName").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional(),
                                fieldWithPath("data.[]phoneNumber").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional()
                        )
                ));

    }

    @Test
    void account() throws Exception {
        //given
        AccountCreate request = AccountCreate.builder()
                .email("manager@gmail.com")
                .bankName("??????")
                .accountNumber("010-8349-0705")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/member/account")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("?????????")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("bankName").description("?????????")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("accountNumber").description("????????????")
                                        .type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.bankName").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .optional(),
                                fieldWithPath("data.accountNumber").description("????????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional()
                        )
                ));
    }

    @Test
    @DisplayName("????????????")
    @Disabled
    void accountUpdate() throws Exception {
        //given
        AccountEdit request = AccountEdit.builder().bankName("??????").accountNumber("010-8349-0706").build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/account/{memberEmail}", "manager@gmail.com")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member/account/patch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberEmail").description("?????????")
                        ),
                        requestFields(
                                fieldWithPath("bankName").description("?????????")
                                        .type(JsonFieldType.STRING)
                                        .optional(),
                                fieldWithPath("accountNumber").description("????????? ????????????")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }

    //todo
    //?????? ?????? ?????? ??????
    //?????? ?????? ??????(??????)
    //?????? ?????? ??????(??????/??????)

}