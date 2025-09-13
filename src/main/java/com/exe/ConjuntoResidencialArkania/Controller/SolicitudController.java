package main.java.com.exe.ConjuntoResidencialArkania.Controller;

import java.time.LocalDateTime;
import java.util.List;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;

import main.java.com.exe.ConjuntoResidencialArkania.Service.SolicitudesService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    private final SolicitudesService solicitudesService;

    @Autowired
    public SolicitudController(SolicitudesService solicitudesService) {
        this.solicitudesService = solicitudesService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudesDTO>> listarTodasSolicitudes() {
        List<SolicitudesDTO> solicitudes = solicitudesService.listarSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{idSolicitud}")
    public ResponseEntity<SolicitudesDTO> obtenerPorId(@PathVariable Long idSolicitud) {
        return solicitudesService.findById(idSolicitud)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitudesDTO> crearSolicitud(@Valid @RequestBody SolicitudesDTO solicitudDTO) {
        SolicitudesDTO creado = solicitudesService.guardarSolicitud(solicitudDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{idSolicitud}")
    public ResponseEntity<SolicitudesDTO> actualizarSolicitud(@PathVariable Long idSolicitud,
            @Valid @RequestBody SolicitudesDTO dto) {
        SolicitudesDTO actualizado = solicitudesService.editarSolicitud(idSolicitud, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{idSolicitud}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idSolicitud) {
        solicitudesService.eliminarSolicitud(idSolicitud);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-fecha-creacion")
    public ResponseEntity<List<SolicitudesDTO>> obtenerPorFechaCreacion(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(solicitudesService.listarPorFechaCreacion(inicio, fin));
    }

    @GetMapping("/por-fecha-resolucion")
    public ResponseEntity<List<SolicitudesDTO>> obtenerPorFechaResolucion(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(solicitudesService.listarPorFechaResolucion(inicio, fin));
    }

    @GetMapping("/por-estados")
    public ResponseEntity<List<SolicitudesDTO>> obtenerPorEstados(
            @RequestParam List<SolicitudesEntity.EstadoSolicitud> estados) {

        return ResponseEntity.ok(solicitudesService.listarPorEstados(estados));
    }

    @GetMapping("/por-tipos")
    public ResponseEntity<List<SolicitudesDTO>> obtenerPorTipos(
            @RequestParam List<SolicitudesEntity.TipoSolicitud> tipos) {

        return ResponseEntity.ok(solicitudesService.listarPorTipos(tipos));
    }

    @GetMapping("/buscar-descripcion")
    public ResponseEntity<List<SolicitudesDTO>> buscarPorDescripcion(@RequestParam String descripcion) {
        return ResponseEntity.ok(solicitudesService.buscarPorDescripcion(descripcion));
    }

    @GetMapping("/filtrar-estado-y-tipo")
    public ResponseEntity<List<SolicitudesDTO>> filtrar(
            @RequestParam List<SolicitudesEntity.EstadoSolicitud> estados,
            @RequestParam List<SolicitudesEntity.TipoSolicitud> tipos) {

        return ResponseEntity.ok(solicitudesService.filtrarPorEstadosYTipos(estados, tipos));
    }
}
