package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.demo.models.ImovelModel;
import com.example.demo.dtos.SolicitacaoSaberMaisDTO;
import com.example.demo.dtos.StatusRequest;
import com.example.demo.service.EmailService;
import com.example.demo.service.ImovelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {
    private final ImovelService service;
    private final EmailService emailService;

    public ImovelController(ImovelService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<ImovelModel>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelModel> find(@PathVariable Integer id) {
        ImovelModel model = service.find(id);
        return model == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(model);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ImovelModel> alterarStatus(@PathVariable Integer id,
            @Valid @RequestBody StatusRequest request) {
        ImovelModel model = service.find(id);
        if (model == null) return ResponseEntity.notFound().build();
        model.setStatus(request.status().trim());
        return ResponseEntity.ok(service.update(model));
    }

    @PostMapping
    public ResponseEntity<ImovelModel> insert(@RequestBody ImovelModel model) {
        ImovelModel saved = service.insert(model);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PostMapping("/sabermais/{id}")
    public ResponseEntity<String> saberMais(
            @PathVariable Integer id,
            @Valid @RequestBody SolicitacaoSaberMaisDTO solicitacao) {

        ImovelModel imovel = service.find(id);
        if (imovel == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            emailService.enviarEmailComTemplate(
                    solicitacao.email(),
                    "Informações sobre o imóvel: " + imovel.getTitulo(),
                    "email/saber-mais-imovel",
                    Map.of(
                            "cliente", solicitacao,
                            "imovel", imovel,
                            "mensagemImobiliaria",
                            "Obrigado pelo seu interesse! Nossa equipe entrará em contato em breve para ajudar com este imóvel."));

            return ResponseEntity.ok("E-mail com os dados do imóvel enviado com sucesso.");
        } catch (Exception exception) {
            return ResponseEntity.internalServerError()
                    .body("Não foi possível enviar o e-mail: " + exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelModel> update(@RequestBody ImovelModel model, @PathVariable Integer id) {
        model.setId(id);
        ImovelModel saved = service.update(model);
        return saved == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
