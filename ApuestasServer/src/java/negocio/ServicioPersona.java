/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import datos.Persona;
import datos.repositorio.Repositorio;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author timoteo
 */
public class ServicioPersona {
    
    private Logger log = LoggerFactory.getLogger(ServicioPersona.class);
    @Inject
    Repositorio repositorio;
    
    public void registrarPersona(Persona persona) {
        if (seleccionarPorNombre(persona.getNombre()).isEmpty()) {
            repositorio.guardar(persona);
        } else {
            log.info("Nombre de persona duplicado: " + persona.getNombre());
        }
    }
    
    private List<Persona> seleccionarPorNombre(String nombre) {
        return repositorio.seleccionarPorPropiedad(Persona.class, "nombre", nombre);
    }

    public List<Persona> seleccionarTodos() {
        return repositorio.seleccionarTodos(Persona.class);
    }
}
