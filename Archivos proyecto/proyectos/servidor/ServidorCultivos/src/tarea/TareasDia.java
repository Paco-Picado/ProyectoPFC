package tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TareasDia {
	private String fecha;
	private ArrayList<Tarea> listaTareas;
	//Este objeto representa la lista de tareas de una fecha en concreto
	public TareasDia(LocalDate dia) {
		listaTareas = new ArrayList<Tarea>();
		fecha = dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	public void anhadirTarea(Tarea tarea) {
		listaTareas.add(tarea);
	}
	public String getFecha() {
		return fecha;
	}	
	public ArrayList<Tarea> getListaTareas() {
		return listaTareas;
	}
	//Devuelve si lista tareas esta vaciaS
	public boolean isEmptyListaTareas() {
		return listaTareas.size() == 0;
	}
}