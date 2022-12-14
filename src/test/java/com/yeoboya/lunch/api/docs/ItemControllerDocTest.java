package com.yeoboya.lunch.api.docs;

import com.yeoboya.lunch.api.container.ContainerDI;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
class ItemControllerDocTest extends ContainerDI {

    @Test
    void create() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .shopName("맥도날드")
                .itemName("더블불고기버거")
                .price(7300)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/item")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("item/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("식당이름"),
                                fieldWithPath("itemName").description("메뉴이름").attributes(key("note").value("중복 안됨")),
                                fieldWithPath("price").description("가격")
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonFieldType.NUMBER)
                                        .description("code"),
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("message"),
                                fieldWithPath("data.shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("가게이름")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.name")
                                        .type(JsonFieldType.STRING)
                                        .description("상품이름")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("상품가격")
                                        .attributes(key("length").value("20"))
                        )
                ));
    }

    @Test
    void getItem() throws Exception {

        mockMvc.perform(get("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("item/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.shopName") .description("상점명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.name") .description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }


    @Test
    void getList() throws Exception {

        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
//        info.add("page", "0");
//        info.add("size", "10");

        mockMvc.perform(get("/item")
                        .params(info))
                .andExpect(status().isOk())
                .andDo(document("item/get/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.[].shopName").description("상점명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].name").description("상품명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
                                        .attributes(key("length").value("20"))
                        )
                ));
    }


    @Test
    @Transactional
    void updateItem() throws Exception {

        //given
        ItemEdit request = ItemEdit.builder().name("NEW 슈비버거").price(10500).build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("item/patch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                                )
                ));
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/item/{itemId}", 8)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("item/delete",
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }
}