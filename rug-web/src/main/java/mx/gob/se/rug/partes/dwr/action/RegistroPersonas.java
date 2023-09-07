/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.gob.se.rug.partes.dwr.action;

/**
 *
 * @author talo4
 */
import java.util.ArrayList;
import java.util.List;

public class RegistroPersonas {
    private List<Persona> personas = new ArrayList<>();

    public List<Persona> getPersonas() {
        return personas;
    }

    public void agregarPersona(Persona persona) {
        personas.add(persona);
    }
}
