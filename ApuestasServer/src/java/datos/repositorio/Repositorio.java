/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.repositorio;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author timoteo
 */
public class Repositorio {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> seleccionarTodos(Class<T> clazz) {
        return seleccionarPorPropiedad(clazz, Collections.EMPTY_MAP);
    }

    public <T> List<T> seleccionarPorId(Class<T> clazz, Integer id) {
        T result = entityManager.find(clazz, id);
        List<T> list = new ArrayList<T>();
        if (result != null) {
            list.add(result);
        }
        return list;
    }

    public <T> List<T> seleccionarPorPropiedad(Class<T> clazz, String propiedad, Object valor) {
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(propiedad, valor);
        return seleccionarPorPropiedad(clazz, props);
    }

    public <T> List<T> seleccionarPorPropiedad(Class<T> clazz, Map<String, Object> propiedades) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM " + clazz.getSimpleName() + " t ");
        if (propiedades != null && !propiedades.isEmpty()) {
            queryStr.append(" WHERE ");
            for (Iterator<String> it = propiedades.keySet().iterator(); it.hasNext();) {
                String prop = it.next();
                queryStr.append(prop + "=" + ":" + prop + " ");
                queryStr.append(it.hasNext() ? " AND " : "");
            }
        }
        Query query = entityManager.createQuery(queryStr.toString());
        if (propiedades != null && !propiedades.isEmpty()) {
            for (Map.Entry<String, Object> entry : propiedades.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    public <T> void guardar(T entidad) {
        entityManager.persist(entidad);
    }

    public <T> T combinar(T entidad) {
        return entityManager.merge(entidad);
    }

    public <T> void revertir(T entidad) {
        combinar(entidad);
        entityManager.refresh(entidad);
    }

    public <T> void eliminar(T entidad) {
        entityManager.remove(entidad);
    }
}
