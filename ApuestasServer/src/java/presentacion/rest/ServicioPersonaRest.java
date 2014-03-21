/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion.rest;

import datos.Persona;
import java.util.Arrays;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import negocio.ServicioPersona;

/**
 *
 * @author timoteo
 */
@Stateless
@LocalBean
@Named
@Path("/personas")
public class ServicioPersonaRest {
    
    @Inject
    ServicioPersona servicioPersona;
    
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> list() {
        Persona persona = new Persona();
        persona.setNombre("persona: " + System.currentTimeMillis());
        servicioPersona.registrarPersona(persona);
        return servicioPersona.seleccionarTodos();
        //return Arrays.asList(persona);
    }

        
}
