package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.models.ImovelModel;
import com.example.demo.repositories.ImovelRepository;

@Service
public class ImovelService {
    private final ImovelRepository repository;

    public ImovelService(ImovelRepository repository) { this.repository = repository; }
    public List<ImovelModel> getAll() { return repository.findAll(); }
    public ImovelModel find(Integer id) { return repository.findById(id).orElse(null); }

    public ImovelModel insert(ImovelModel model) {
        model.setId(null);
        return repository.save(model);
    }

    public ImovelModel update(ImovelModel model) {
        if (model.getId() == null || !repository.existsById(model.getId())) return null;
        return repository.save(model);
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
