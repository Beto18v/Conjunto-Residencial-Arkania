package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.ParqueaderoDTO;
import com.exe.ConjuntoResidencialArkania.Service.ParqueaderoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parqueaderos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParqueaderoController {

    private final ParqueaderoService parqueaderoService;

    @GetMapping
    public ResponseEntity<List<ParqueaderoDTO>> obtenerTodos() {
        List<ParqueaderoDTO> parqueaderos = parqueaderoService.obtenerTodos();
        return ResponseEntity.ok(parqueaderos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParqueaderoDTO> obtenerPorId(@PathVariable Long id) {
        return parqueaderoService.obtenerPorId(id)
                .map(parqueadero -> ResponseEntity.ok(parqueadero))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParqueaderoDTO> crear(@RequestBody ParqueaderoDTO parqueaderoDTO) {
        try {
            ParqueaderoDTO creado = parqueaderoService.crear(parqueaderoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParqueaderoDTO> actualizar(@PathVariable Long id,
                                                     @RequestBody ParqueaderoDTO parqueaderoDTO) {
        try {
            ParqueaderoDTO actualizado = parqueaderoService.actualizar(id, parqueaderoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            parqueaderoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}