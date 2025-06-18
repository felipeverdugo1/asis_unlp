package service;

import dao.GenericDAO;
import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final GenericDAO<T, ID> genericDAO;

    public GenericServiceImpl(GenericDAO<T, ID> genericDAO) {
        this.genericDAO = genericDAO;
    }


    @Override
    public T crear(T entidad) {
        genericDAO.crear(entidad);
        return entidad;
    }

    @Override
    public T buscarPorId(ID id) {
        return genericDAO.buscarPorId(id);
    }

    @Override
    public T actualizar(T entidad) {
        genericDAO.actualizar(entidad);
        return entidad;
    }

    @Override
    public void eliminar(ID id) {
        genericDAO.eliminar(id);
    }

    @Override
    public List<T> listarTodos() {
        return genericDAO.listarTodos();
    }

    @Override
    public Optional<T> buscarPorCampo(String campo, Object valor) {
        return genericDAO.buscarPorCampo(campo, valor);
    }

    @Override
    public List<T> buscarTodosPorCampoLike(String campo, Object patron) {
        return genericDAO.buscarTodosPorCampoLike(campo, patron);
    }

    @Override
    public void flush(){
        genericDAO.flush();
    }
}