public class Puntero {
    private int puntero;
    public Puntero(int puntero) {
        this.puntero = puntero;
    }
    public int getPuntero() {
        return puntero;
    }
    public void avanzar() {
        puntero++;
    }
    public void retroceder(){
        puntero--;
    }
}
