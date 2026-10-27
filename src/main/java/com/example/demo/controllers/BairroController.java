package com.example.demo.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.models.BairroModel;
import com.example.demo.service.BairroService;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    @Autowired
    private BairroService service;

    @GetMapping
    public ResponseEntity<List<BairroModel>> getAllBairros() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/bairros-page")
    public ResponseEntity<Page<BairroModel>> getBairros(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BairroModel> find(@PathVariable Integer id) {
        BairroModel model = service.find(id);
        return model == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<BairroModel> insert(@RequestBody BairroModel model) {
        BairroModel saved = service.insert(model);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroModel> update(@RequestBody BairroModel model, @PathVariable Integer id) {
        model.setId(id);
        BairroModel saved = service.update(model);
        return saved == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
