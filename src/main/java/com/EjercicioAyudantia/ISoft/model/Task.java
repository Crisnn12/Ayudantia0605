package com.EjercicioAyudantia.ISoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private Long id;
    private String titulo;
    private String prioridad;
    private String fechaLimite;

    @JsonProperty("completada")
    private Boolean completada = false;

    public Task() {
    }

    public Task(Long id, String titulo, String prioridad, String fechaLimite, Boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.completada = completada != null ? completada : false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Boolean isCompletada() {
        return completada != null ? completada : false;
    }

    public Boolean getCompletada() {
        return completada != null ? completada : false;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada != null ? completada : false;
    }
}
