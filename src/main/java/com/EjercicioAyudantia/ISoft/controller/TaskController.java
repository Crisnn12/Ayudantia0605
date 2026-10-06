package com.EjercicioAyudantia.ISoft.controller;

import java.util.List;
import com.EjercicioAyudantia.ISoft.model.Task;
import com.EjercicioAyudantia.ISoft.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
public List<Task> obtenerTareas(
        @RequestParam(name = "prioridad", required = false)
        String prioridad,

        @RequestParam(name = "titulo", required = false)
        String titulo,

        @RequestParam(name = "fechaLimite", required = false)
        String fechaLimite) {

    return taskService.filtrarTareas(
            prioridad,
            titulo,
            fechaLimite);
}
}
