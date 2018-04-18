package com.perales.sepomex.contract;

import org.springframework.data.domain.Page;

public interface ServiceGeneric<T, PK> {

    public T buscarPorId(PK id);

    public Page<T> buscarTodos(int page, int size);

    public T guardar(T entity);

    public T actualizar(T entity);

    public T borrar(PK id);

}
