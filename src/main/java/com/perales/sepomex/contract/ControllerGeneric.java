package com.perales.sepomex.contract;

import java.util.List;

public interface ControllerGeneric<T, PK> {

    public T buscarPorId(PK id);

    public List<T> buscarTodos(int page, int size);

    public T guardar(T entity);

    public T actualizar(T entity);

    public T borrar(PK id);
}
