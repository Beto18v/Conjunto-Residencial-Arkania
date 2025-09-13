package main.java.com.exe.ConjuntoResidencialArkania.Controller;

import java.util.List;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;

import main.java.com.exe.ConjuntoResidencialArkania.Service.SolicitudesService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
   private final SolicitudesService solicitudesService;
   
   @Autowired
   public SolicitudController(SolicitudesService solicitudesService){
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
    public ResponseEntity<SolicitudesDTO> actualizarSolicitud(@PathVariable Long idSolicitud, @Valid @RequestBody SolicitudesDTO dto) {
        SolicitudesDTO actualizado = solicitudesService.editarSolicitud(idSolicitud, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{idSolicitud}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idSolicitud) {
        solicitudesService.eliminarSolicitud(idSolicitud);
        return ResponseEntity.noContent().build();
    }
}
