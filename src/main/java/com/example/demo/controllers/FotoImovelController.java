package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.FotoImovelModel;
import com.example.demo.service.FotoImovelService;

@RestController
@RequestMapping({ "/fotos", "/fotos-imoveis" })
public class FotoImovelController {

    private final FotoImovelService service;

    public FotoImovelController(FotoImovelService service) { this.service = service; }

    @PostMapping(value = "/imovel/{imovelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@PathVariable Integer imovelId,
            @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            return ResponseEntity.status(201).body(service.salvar(arquivo, imovelId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salvar(@RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("imovelId") Integer imovelId) {
        return upload(imovelId, arquivo);
    }

    @GetMapping("/imovel/{imovelId}")
    public ResponseEntity<List<FotoImovelModel>> listar(@PathVariable Integer imovelId) {
        return ResponseEntity.ok(service.getByImovel(imovelId));
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<?> baixar(@PathVariable Integer id) {
        FotoImovelModel foto = service.find(id);
        if (foto == null) return ResponseEntity.notFound().build();
        try {
            String tipoDetectado = java.nio.file.Files.probeContentType(java.nio.file.Path.of(foto.getCaminho()));
            MediaType tipo = tipoDetectado == null ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(tipoDetectado);
            return ResponseEntity.ok()
                    .contentType(tipo)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + foto.getNomeArquivo().replace("\"", "") + "\"")
                    .body(service.carregar(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", "Nao foi possivel ler a foto."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoImovelModel> atualizar(@PathVariable Integer id,
            @RequestParam(required = false) Boolean capa,
            @RequestParam(required = false) Integer ordem) {
        FotoImovelModel foto = service.atualizar(id, capa, ordem);
        return foto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(foto);
    }

    @PutMapping("/{id}/capa")
    public ResponseEntity<FotoImovelModel> tornarCapa(@PathVariable Integer id) {
        FotoImovelModel foto = service.tornarCapa(id);
        return foto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(foto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", e.getMessage()));
        }
    }
}
