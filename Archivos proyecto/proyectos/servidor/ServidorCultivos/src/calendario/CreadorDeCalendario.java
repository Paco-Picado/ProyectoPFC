package calendario;

import java.time.LocalDate;
import java.util.ArrayList;

import tarea.TareasBD;
import tarea.TareasDia;

public class CreadorDeCalendario {
	private ArrayList<TareasDia> calendario;
	private LocalDate fechaActual;
	public CreadorDeCalendario() {
		calendario = new ArrayList<TareasDia>();
		fechaActual = LocalDate.now();
	}
	public void crearCalendario(ArrayList<TareasBD> listaTareasBD) {
		for(TareasBD t: listaTareasBD) {
			if(t.getDiaInicial() <=0 || t.getDiaFinal() <=0 || t.getIntervaloDias() <=0) {
				//si cualquier dia o intervalo es menor igual que cero se descarta la tarea
			}
			else {
				//Se añaden todos los dias con sus tareas
				anhadirTareasACalendario(t);				
			}		
		}
		//Se quitan los dias sin tareas
		calendario = quitarDiasSinTareas(calendario);
		
	}
	//Rellena el calendario con la tarea a partir de los dias
	private void anhadirTareasACalendario(TareasBD t) {
		//ArrayList<TareasDia> calendario = new ArrayList<TareasDia>();
		for(int i = t.getDiaInicial();i <= t.getDiaFinal(); i+=t.getIntervaloDias()) {
			
			if(i >= calendario.size()) {
				//Se expande el calendario si la posicion del dia es mayor que el propio calendario
				for(int j = calendario.size(); j<=i;++j ) {
					calendario.add(new TareasDia(fechaActual.plusDays(j)));
				}				
			}
			//Se añade una tarea a un dia determinado
			calendario.get(i-1).anhadirTarea(t.getTarea());
		}
	}
	private ArrayList<TareasDia> quitarDiasSinTareas(ArrayList<TareasDia> calendario) {
		ArrayList<TareasDia> nuevoCalendario = new ArrayList<TareasDia>();
		for(TareasDia dt: calendario) {
			if(!dt.isEmptyListaTareas()) {
				nuevoCalendario.add(dt);
			}
		}
		return nuevoCalendario;
	}
	public ArrayList<TareasDia> getCalendario(){
		return calendario;
	}
}
