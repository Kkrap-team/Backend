package com.Kkrap.endpoint;

import com.Kkrap.Controller.FoldersController;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(FoldersController.class)
public class FoldersEndpointTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private FoldersManagerService foldersManagerService;

    @Test
    @DisplayName("빈 이름을 전달하면, IllegalArgumentException 에러가 발생한다.")
    void nullPointName( ) {
        FoldersCreateRequest request = FoldersCreateRequest.of("", "설명", true, true);
        Long userId = 12345L;

        Assertions.assertThrows(IllegalArgumentException.class ,() -> {
            mockMvc.perform(post("/folders/users/{userId}/folders"+userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        });
    }

    @Test
    @DisplayName("빈 설명을 전달하면, IllegalArgumentException 에러가 발생한다.")
    void nullPointDescription( ) {
        FoldersCreateRequest request = FoldersCreateRequest.of("제목", "", true, true);
        Long userId = 12345L;

        Assertions.assertThrows(IllegalArgumentException.class ,() -> {
            mockMvc.perform(post("/folders/users/{userId}/folders"+userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        });
    }
}
