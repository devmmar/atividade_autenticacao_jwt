package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.FotoImovelModel;
import com.example.demo.models.ImovelModel;
import com.example.demo.repositories.FotoImovelRepository;
import com.example.demo.repositories.ImovelRepository;

@Service
public class FotoImovelService {

    private final FotoImovelRepository repository;
    private final ImovelRepository imovelRepository;
    private final Path uploadDir;

    public FotoImovelService(FotoImovelRepository repository,
            ImovelRepository imovelRepository,
            @Value("${app.upload-dir}") String uploadDir) {
        this.repository = repository;
        this.imovelRepository = imovelRepository;
        this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public List<FotoImovelModel> getByImovel(Integer imovelId) {
        return repository.findByImovelId(imovelId);
    }

    public FotoImovelModel find(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public FotoImovelModel atualizar(Integer id, Boolean capa, Integer ordem) {
        FotoImovelModel foto = find(id);
        if (foto == null) return null;

        if (ordem != null) foto.setOrdem(ordem);
        if (capa != null) {
            if (capa) removerCapaDasOutrasFotos(foto);
            foto.setCapa(capa);
        }
        return repository.save(foto);
    }

    @Transactional
    public FotoImovelModel tornarCapa(Integer id) {
        FotoImovelModel foto = find(id);
        if (foto == null) return null;
        removerCapaDasOutrasFotos(foto);
        foto.setCapa(true);
        return repository.save(foto);
    }

    @Transactional
    public FotoImovelModel salvar(MultipartFile arquivo, Integer imovelId) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione uma imagem.");
        }
        ImovelModel imovel = imovelRepository.findById(imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imovel nao encontrado."));
        String original = StringUtils.cleanPath(
                arquivo.getOriginalFilename() == null ? "imagem" : arquivo.getOriginalFilename());
        String nomeSalvo = UUID.randomUUID() + "_" + original;
        Path diretorioImovel = uploadDir.resolve(String.valueOf(imovelId)).normalize();
        Path destino = diretorioImovel.resolve(nomeSalvo).normalize();
        if (!diretorioImovel.getParent().equals(uploadDir)
                || !destino.getParent().equals(diretorioImovel)) {
            throw new IllegalArgumentException("Nome de arquivo invalido.");
        }

        try {
            Files.createDirectories(diretorioImovel);
            arquivo.transferTo(destino);

            FotoImovelModel foto = new FotoImovelModel();
            foto.setNomeArquivo(nomeSalvo);
            foto.setCaminho(destino.toString());
            foto.setCapa(false);
            foto.setOrdem(0);
            foto.setImovel(imovel);
            return repository.save(foto);
        } catch (Exception e) {
            Files.deleteIfExists(destino);
            if (e instanceof IOException ioException) throw ioException;
            throw new IOException("Nao foi possivel salvar a foto.", e);
        }
    }

    @Transactional(readOnly = true)
    public byte[] carregar(Integer id) throws IOException {
        FotoImovelModel foto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foto nao encontrada."));
        Path arquivo = resolverArquivo(foto);
        return Files.readAllBytes(arquivo);
    }

    @Transactional
    public boolean delete(Integer id) {
        FotoImovelModel foto = find(id);
        if (foto == null) return false;

        Path arquivo = resolverArquivo(foto);
        try {
            repository.delete(foto);
            repository.flush();
            Files.deleteIfExists(arquivo);
            return true;
        } catch (Exception e) {
            // Excecao nao verificada faz o Spring reverter a exclusao do banco.
            throw new IllegalStateException("Nao foi possivel excluir a foto.", e);
        }
    }

    private Path resolverArquivo(FotoImovelModel foto) {
        Path arquivo = Path.of(foto.getCaminho()).toAbsolutePath().normalize();
        if (!arquivo.startsWith(uploadDir)) {
            throw new IllegalArgumentException("Caminho de arquivo invalido.");
        }
        return arquivo;
    }

    private void removerCapaDasOutrasFotos(FotoImovelModel fotoSelecionada) {
        List<FotoImovelModel> fotos = repository.findByImovelId(fotoSelecionada.getImovel().getId());
        for (FotoImovelModel foto : fotos) {
            if (!foto.getId().equals(fotoSelecionada.getId()) && Boolean.TRUE.equals(foto.getCapa())) {
                foto.setCapa(false);
            }
        }
        repository.saveAll(fotos);
    }
}
