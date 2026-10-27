package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.demo.dtos.UserDTO;
import com.example.demo.dtos.CreateUserRequest;
import com.example.demo.models.UserModel;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    // GET /users - a resposta agora e uma lista de DTOs (sem a senha)
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserModel> listaNormal = service.getAllUsers();
        List<UserDTO> listaDtos = listaNormal.stream().map(usuario -> new UserDTO(usuario))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(listaDtos);
    }

    // GET /users/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> find(@PathVariable Integer id) {
        UserModel model = service.find(id);

        // sem esta checagem, um id inexistente estouraria NullPointerException
        // dentro de new UserDTO(model)
        if (model == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserDTO dto = new UserDTO(model);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // POST /users - responde 201 com o DTO criado (sem a senha) e o
    // endereco do novo recurso no header Location
    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody CreateUserRequest request) {
        UserModel model = service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(model.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDTO(model));
    }

    // PUT /users/{id} - responde 200 com o DTO atualizado (sem a senha)
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto, @PathVariable Integer id) {
        dto.setId(id);
        UserModel model = service.update(dto);

        if (model == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(model));
    }

    // DELETE /users/{id}
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
