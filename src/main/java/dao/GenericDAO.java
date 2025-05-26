package dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    // Operaciones CRUD básicas
    void crear(T entidad);
    T buscarPorId(ID id);
    void actualizar(T entidad);
    void eliminar(ID id);
    List<T> listarTodos();
}