package service;
import java.util.List;


public interface GenericService<T, ID> {
    T crear(T entidad);
    T buscarPorId(ID id);
    T actualizar(T entidad);
    void eliminar(ID id);
    List<T> listarTodos();
    T buscarPorCampo(String campo, Object valor);

}


