package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.ApartamentoDTO;
import com.exe.ConjuntoResidencialArkania.Service.ApartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApartamentoController {

    private final ApartamentoService apartamentoService;

    @GetMapping
    public ResponseEntity<List<ApartamentoDTO>> obtenerTodos() {
        List<ApartamentoDTO> apartamentos = apartamentoService.obtenerTodos();
        return ResponseEntity.ok(apartamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartamentoDTO> obtenerPorId(@PathVariable Long id) {
        return apartamentoService.obtenerPorId(id)
                .map(apartamento -> ResponseEntity.ok(apartamento))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApartamentoDTO> crear(@RequestBody ApartamentoDTO apartamentoDTO) {
        try {
            ApartamentoDTO creado = apartamentoService.crear(apartamentoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartamentoDTO> actualizar(@PathVariable Long id,
                                                     @RequestBody ApartamentoDTO apartamentoDTO) {
        try {
            ApartamentoDTO actualizado = apartamentoService.actualizar(id, apartamentoDTO);
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
            apartamentoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}