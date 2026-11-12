public class LotePapelCarton extends Lote {

    private boolean clasificado;

    public LotePapelCarton(String codigo, String descripcion, int pesoKg, boolean clasificado) {
        super(codigo, descripcion, pesoKg);
        this.clasificado = clasificado;
    }

    @Override
    public int calcularValor() {
        int tarifa = 500;
        if (this.clasificado) {
            tarifa += 100;
        }
        return this.pesoKg * tarifa;
    }

    public boolean isClasificado() {
        return clasificado;
    }

    public void setClasificado(boolean clasificado) {
        this.clasificado = clasificado;
    }
}