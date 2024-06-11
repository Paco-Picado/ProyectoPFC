package tarea;

public class TareasBD {
	private Tarea tarea;
	private int diaInicial;
	private int diaFinal;
	private int intervaloDias;	
	//Esta clase auxiliar ayuda a recoger los datos de la BD más facilmente
	public TareasBD(Tarea tarea, int diaInicial, int diaFinal, int intervaloDias) {
		this.tarea = tarea;
		this.diaInicial = diaInicial;
		this.diaFinal = diaFinal;
		this.intervaloDias = intervaloDias;
	}
	public TareasBD() {}
	public Tarea getTarea() {
		return tarea;
	}
	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	public int getDiaInicial() {
		return diaInicial;
	}
	public void setDiaInicial(int diaInicial) {
		this.diaInicial = diaInicial;
	}
	public int getDiaFinal() {
		return diaFinal;
	}
	public void setDiaFinal(int diaFinal) {
		this.diaFinal = diaFinal;
	}
	public int getIntervaloDias() {
		return intervaloDias;
	}
	public void setIntervaloDias(int intervaloDias) {
		this.intervaloDias = intervaloDias;
	}
}
