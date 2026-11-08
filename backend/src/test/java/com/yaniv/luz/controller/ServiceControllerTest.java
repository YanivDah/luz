package com.yaniv.luz.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.Mockito.when;
import java.util.List;
import com.yaniv.luz.dto.ServiceResponse;
import com.yaniv.luz.service.ServiceService;

@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ServiceService serviceService;

    @Test
    void shouldReturnServicesSuccessfully() throws Exception {
        when(serviceService.getServices(1L)).thenReturn(
            List.of(
                new ServiceResponse(1L, "Haircut", 30, 80),
                new ServiceResponse(2L, "Haircut + Beard", 45, 110),
                new ServiceResponse(3L, "Long Haircut", 60, 140)
            )
        );
        mockMvc.perform(get("/api/businesses/1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Haircut"))
                .andExpect(jsonPath("$[0].durationMinutes").value(30));
    }

    @Test
    void shouldReturnEmptyListForBusinessWithoutServices() throws Exception {
        when(serviceService.getServices(999L)).thenReturn(List.of());
        mockMvc.perform(get("/api/businesses/999/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}