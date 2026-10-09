package com.EjercicioAyudantia.ISoft.service;

import com.EjercicioAyudantia.ISoft.model.Task;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Locale;

@Service
public class TaskService {

    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Task createTask(Task task) {
        task.setId(idGenerator.getAndIncrement());
        task.setCompletada(false);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId() != null && t.getId().equals(id))
                .findFirst();
    }

    public Optional<Task> completeTask(Long id) {
        return findById(id).map(task -> {
            task.setCompletada(true);
            return task;
        });
    }

    public List<Task> filtrarTareas(
        String prioridad,
        String titulo,
        String fechaLimite) {

    return tasks.stream()
            .filter(task -> prioridad == null
                    || prioridad.equals(task.getPrioridad()))
            .filter(task -> titulo == null
                    || (task.getTitulo() != null
                    && task.getTitulo()
                            .toLowerCase(Locale.ROOT)
                            .contains(titulo.toLowerCase(Locale.ROOT))))
            .filter(task -> fechaLimite == null
                    || fechaLimite.equals(task.getFechaLimite()))
            .toList();
}
}

