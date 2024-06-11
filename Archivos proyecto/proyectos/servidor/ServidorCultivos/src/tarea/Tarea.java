package tarea;

public class Tarea {
	private String nombre;
	private String descripcion;
	//Este objeto contiene el nombre de la tarea y una descripción de lo que hace
	public Tarea(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
}
