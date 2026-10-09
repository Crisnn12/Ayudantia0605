package com.EjercicioAyudantia.ISoft;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCompleteTaskUpdatesSharedStateAndPreservesOtherTasks() throws Exception {
        String taskJson = """
            {
              "titulo": "Revisar API",
              "prioridad": "ALTA",
              "fechaLimite": "2025-06-30"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"titulo": "Otra tarea", "prioridad": "BAJA"}
                            """))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/tasks/{id}/complete", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Revisar API"))
                .andExpect(jsonPath("$.prioridad").value("ALTA"))
                .andExpect(jsonPath("$.fechaLimite").value("2025-06-30"))
                .andExpect(jsonPath("$.completada").value(true));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].completada").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].completada").value(false));
    }

    @Test
    void testCompleteTaskReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(patch("/tasks/{id}/complete", 999))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testCompleteTaskCanBeRepeated() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"titulo": "Tarea repetida", "prioridad": "MEDIA"}
                            """))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/tasks/{id}/complete", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completada").value(true));
        mockMvc.perform(patch("/tasks/{id}/complete", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.completada").value(true));
    }

    @Test
    void testCreateTaskSuccess() throws Exception {
        String taskJson = """
            {
              "titulo": "Revisar documentación de la API",
              "prioridad": "ALTA",
              "fechaLimite": "2025-06-30"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Revisar documentación de la API"))
                .andExpect(jsonPath("$.prioridad").value("ALTA"))
                .andExpect(jsonPath("$.fechaLimite").value("2025-06-30"))
                .andExpect(jsonPath("$.completada").value(false));
    }

    @Test
    void testCreateTaskWithoutFechaLimite() throws Exception {
        String taskJson = """
            {
              "titulo": "Tarea sin fecha limite",
              "prioridad": "MEDIA"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Tarea sin fecha limite"))
                .andExpect(jsonPath("$.prioridad").value("MEDIA"))
                .andExpect(jsonPath("$.fechaLimite").doesNotExist())
                .andExpect(jsonPath("$.completada").value(false));
    }

    @Test
    void testCreateTaskAutoIncrementsIdAndForcesCompletadaFalse() throws Exception {
        String task1 = """
            {
              "titulo": "Primera tarea",
              "prioridad": "ALTA",
              "fechaLimite": "2025-07-01",
              "completada": true
            }
            """;

        String task2 = """
            {
              "titulo": "Segunda tarea",
              "prioridad": "BAJA",
              "fechaLimite": null
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.completada").value(false));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.titulo").value("Segunda tarea"))
                .andExpect(jsonPath("$.prioridad").value("BAJA"))
                .andExpect(jsonPath("$.completada").value(false));
    }
}
