package service;
import java.util.List;
import java.util.Optional;


public interface GenericService<T, ID> {
    T crear(T entidad);
    Optional<T> buscarPorId(ID id);
    T actualizar(T entidad);
    void eliminar(T entidad);
    List<T> listarTodos();
    Optional<T> buscarPorCampo(String campo, Object valor);
    List<T> buscarTodosPorCampoLike(String campo, Object patron);
    void flush();
}


