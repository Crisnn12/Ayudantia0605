package com.EjercicioAyudantia.ISoft.service;

import com.EjercicioAyudantia.ISoft.model.Task;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
}
