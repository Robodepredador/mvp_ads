package com.example.farmaceutica;

import com.example.farmaceutica.domain.*;
import com.example.farmaceutica.services.ProgramacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProgramacionFlowTest {

    @Autowired
    private ProgramacionService programacionService;

    @Autowired
    private RequerimientoRepository reqRepo;

    @Autowired
    private DetalleRequerimientoRepository detReqRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private LoteProductoRepository loteRepo;

    @Test
    void testFlujoCompletoProgramacion() {
        // 1. Setup: Crear datos de prueba
        Producto p1 = new Producto();
        p1.setNombre("Paracetamol 500mg");
        p1.setCodigo("P001");
        p1.setPrecioReferencial(new java.math.BigDecimal("10.00"));
        p1 = productoRepo.save(p1);

        LoteProducto lote = new LoteProducto();
        lote.setProducto(p1);
        lote.setNumeroLote("L-TEST-001");
        lote.setCantidad(100);
        lote.setFechaVencimiento(java.time.LocalDate.now().plusYears(1));
        lote = loteRepo.save(lote);

        Requerimiento req = new Requerimiento();
        req.setEstado("PENDIENTE");
        req.setPrioridad("ALTA");
        req.setFechaSolicitud(java.time.LocalDateTime.now());
        req = reqRepo.save(req);

        DetalleRequerimiento det = new DetalleRequerimiento();
        det.setRequerimiento(req);
        det.setProducto(p1);
        det.setCantidad(50); // Solicitamos 50
        detReqRepo.save(det);

        // 2. Verificar estado inicial
        Requerimiento reqInicial = programacionService.obtenerDetalleRequerimiento(req.getId());
        assertEquals("PENDIENTE", reqInicial.getEstado());

        // 3. Registrar decisión: Distribuir 20 del lote existente
        programacionService.registrarDecisionDistribucion(req.getId(), p1.getId(), 20, lote.getId(),
                "Stock disponible");

        // Verificar estado parcial
        Requerimiento reqParcial = programacionService.obtenerDetalleRequerimiento(req.getId());
        assertEquals("PARCIAL", reqParcial.getEstado());

        // 4. Intentar finalizar (debería fallar porque faltan 30)
        assertThrows(IllegalStateException.class, () -> {
            programacionService.finalizarProgramacion(req.getId());
        });

        // 5. Registrar decisión: Comprar los 30 restantes
        programacionService.registrarDecisionCompra(req.getId(), p1.getId(), 30, "Faltante para compra");

        // 6. Finalizar correctamente
        String resultado = programacionService.finalizarProgramacion(req.getId());

        // 7. Verificaciones finales
        Requerimiento reqFinal = programacionService.obtenerDetalleRequerimiento(req.getId());
        assertEquals("PROGRAMADO", reqFinal.getEstado());
        assertTrue(resultado.contains("PROGRAMADO"));

        System.out.println("TEST COMPLETADO EXITOSAMENTE: El flujo de programación funcionó correctamente.");
    }
}
