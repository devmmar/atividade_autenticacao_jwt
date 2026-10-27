package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.dtos.UserDTO;
import com.example.demo.dtos.CreateUserRequest;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserModel create(CreateUserRequest request) {
        UserModel user = new UserModel();
        user.setNome(request.nome().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setSenha(passwordEncoder.encode(request.password()));
        user.setTipo(request.tipo());
        return repository.save(user);
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> lista = repository.findAll();
        return lista;
    }

    public UserModel find(Integer id) {
        Optional<UserModel> user = repository.findById(id);
        return user.orElse(null);
    }

    public UserModel insert(UserModel user) {
        return repository.save(user);
    }

    public UserModel update(UserModel user) {
        try {
            UserModel usuarioExiste = find(user.getId());

            if (usuarioExiste == null){
                return null;
            }
            else{
                if (user.getNome() != null){
                    usuarioExiste.setNome(user.getNome());
                }
                return repository.save(usuarioExiste);
            }
        }
        catch(Exception e) {
            return null;
        }
    }

    // ---- versoes que recebem DTO (Aula 06) ----

    public UserModel update(UserDTO dto) {
        UserModel model = find(dto.getId());
        try {
            if (model != null) {
                model.setNome(dto.getNome());
                model.setEmail(dto.getEmail());
                model.setTipo(dto.getTipo());
                return repository.save(model);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(Integer id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
}
