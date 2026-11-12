import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class EcorutaTest {

    // 1. Registro de una entrega
    @Test
    public void probarRegistroEntrega() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");

        Aportante aportanteRegistrado = ecoRuta.consultarAportanteId("1094");
        Aportante aportanteDesconocido = new Aportante("9999", "Pedro Gomez", "pedro@correo.com");

        // Crear entrega E01 con aportante registrado
        boolean registroE01 = ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportanteRegistrado);
        Entrega entregaE01 = ecoRuta.consultarEntregaCodigo("E01");

        assertTrue(registroE01);
        assertNotNull(entregaE01);
        assertEquals(EstadoEntrega.ABIERTA, entregaE01.getEstado());
        assertTrue(entregaE01.getListLotes().isEmpty());
        assertNull(entregaE01.getTheComprobante());

        // Intentar registrar otra entrega con el código repetido E01
        boolean registroRepetido = ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportanteRegistrado);
        assertFalse(registroRepetido);

        // Intentar registrar una entrega con código nuevo y aportante desconocido
        boolean registroDesconocido = ecoRuta.registrarEntrega("E02", LocalDate.of(2026, 11, 10), aportanteDesconocido);
        assertFalse(registroDesconocido);

        // Verificar que únicamente se conserva E01
        assertEquals(1, ecoRuta.getListEntregas().size());
        assertEquals(entregaE01, ecoRuta.consultarEntregaCodigo("E01"));
    }

    // 2. Validación de lotes
    @Test
    public void probarValidacionLotes() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");
        Aportante aportante = ecoRuta.consultarAportanteId("1094");
        ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportante);
        Entrega entrega = ecoRuta.consultarEntregaCodigo("E01");

        // Agregar lote L01 con 5 kg
        Lote lote1 = new LotePapelCarton("L01", "Cajas de carton", 5, false);
        boolean agregadoL01 = entrega.agregarLote(lote1);
        assertTrue(agregadoL01);

        // Intentar agregar otro lote con el mismo código L01
        Lote loteCodigoRepetido = new LoteMetal("L01", "Aluminio", 3, TipoMetal.ALUMINIO);
        boolean agregadoRepetido = entrega.agregarLote(loteCodigoRepetido);
        assertFalse(agregadoRepetido);

        // Intentar agregar un lote distinto con 0 kg
        Lote lotePesoCero = new LoteMetal("L02", "Acero", 0, TipoMetal.ACERO);
        boolean agregadoPesoCero = entrega.agregarLote(lotePesoCero);
        assertFalse(agregadoPesoCero);

        // La entrega debe conservar únicamente el primer lote
        assertEquals(1, entrega.getListLotes().size());
        assertEquals("L01", entrega.getListLotes().get(0).getCodigo());
    }

    // 3. Rechazo de una entrega vacía
    @Test
    public void probarRechazoEntregaVacia() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");
        Aportante aportante = ecoRuta.consultarAportanteId("1094");
        ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportante);
        Entrega entregaVacia = ecoRuta.consultarEntregaCodigo("E01");

        // Intentar aceptar una entrega sin lotes
        boolean aceptada = ecoRuta.aceptarEntrega(entregaVacia);

        assertFalse(aceptada);
        assertEquals(EstadoEntrega.ABIERTA, entregaVacia.getEstado());
        assertNull(entregaVacia.getTheComprobante());
        assertNull(ecoRuta.consultarComprobanteCodigo("E01"));
    }

    // 4. Aceptación y cálculo con los tres tipos de lote
    @Test
    public void probarAceptacionYCalculoTresTiposLote() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");
        Aportante aportante = ecoRuta.consultarAportanteId("1094");
        ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportante);
        Entrega entrega = ecoRuta.consultarEntregaCodigo("E01");

        // 10 kg de papel y cartón clasificado ($6.000)
        Lote lotePapel = new LotePapelCarton("L01", "Papel clasificado", 10, true);
        // 5 kg de aluminio ($11.000)
        Lote loteMetal = new LoteMetal("L02", "Latas de aluminio", 5, TipoMetal.ALUMINIO);
        // 4 kg de residuos electrónicos con 2 equipos completos ($7.200)
        Lote loteElectronico = new LoteResiduoElectronico("L03", "Computadores", 4, 2);

        assertEquals(6000, lotePapel.calcularValor());
        assertEquals(11000, loteMetal.calcularValor());
        assertEquals(7200, loteElectronico.calcularValor());

        entrega.agregarLote(lotePapel);
        entrega.agregarLote(loteMetal);
        entrega.agregarLote(loteElectronico);

        // Aceptar la entrega
        boolean aceptada = ecoRuta.aceptarEntrega(entrega);
        Comprobante comprobante = ecoRuta.consultarComprobanteCodigo("E01");

        assertTrue(aceptada);
        assertEquals(EstadoEntrega.ACEPTADA, entrega.getEstado());
        assertNotNull(comprobante);
        assertEquals(19, comprobante.pesoTotalKg());
        assertEquals(24200, comprobante.valorTotalReconocido());
    }

    // 5. Consulta de etiquetas
    @Test
    public void probarConsultaEtiquetas() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");
        Aportante aportante = ecoRuta.consultarAportanteId("1094");
        ecoRuta.registrarEntrega("E01", LocalDate.of(2026, 11, 10), aportante);
        Entrega entrega = ecoRuta.consultarEntregaCodigo("E01");

        entrega.agregarLote(new LotePapelCarton("L01", "Papel clasificado", 10, true));
        entrega.agregarLote(new LoteMetal("L02", "Latas de aluminio", 5, TipoMetal.ALUMINIO));
        entrega.agregarLote(new LoteResiduoElectronico("L03", "Computadores", 4, 2));

        ecoRuta.aceptarEntrega(entrega);

        LinkedList<String> etiquetas = ecoRuta.consultarEtiquetasEntrega("E01");

        // Deben aparecer exactamente dos etiquetas
        assertEquals(2, etiquetas.size());

        // La del lote de metal incluye su código (L02) y el tipo ALUMINIO
        assertTrue(etiquetas.get(0).contains("L02"));
        assertTrue(etiquetas.get(0).contains("ALUMINIO"));

        // La del lote electrónico incluye su código (L03) y la cantidad de equipos completos (2)
        assertTrue(etiquetas.get(1).contains("L03"));
        assertTrue(etiquetas.get(1).contains("2"));
    }
}