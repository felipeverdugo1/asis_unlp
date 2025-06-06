package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Metodo para borrar entidades creadas de prueba
// Las mismas son creadas con su campo nombre iniciado en TEST_* para su eliminacion en caso de reinicio
// y perdida del HashMap
public class TestDataRegistry {
    private static final Map<Class<?>, List<Long>> entidadesCreadas = new HashMap<>();

    public static <T> void registrar(Class<T> clazz, Long id) {
        entidadesCreadas.computeIfAbsent(clazz, k -> new ArrayList<>()).add(id);
    }

    public static Map<Class<?>, List<Long>> obtenerTodo() {
        return entidadesCreadas;
    }

    public static void limpiar() {
        entidadesCreadas.clear();
    }
}