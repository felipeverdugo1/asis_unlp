package dao;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    // Operaciones CRUD básicas
    void crear(T entidad);
    Optional<T> buscarPorId(ID id);
    void actualizar(T entidad);
    void eliminar(T entidad);
    List<T> listarTodos();
    void flush();

    Optional<T> buscarPorCampo(String campo, Object valor);
    List<T> buscarTodosPorCampoLike(String campo, Object patron);
}