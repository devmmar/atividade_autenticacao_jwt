package com.example.demo.models;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fotos_imoveis")
public class FotoImovelModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeArquivo;

    private String caminho;
    private Boolean capa;
    private Integer ordem;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "imovel_id", nullable = false)
    private ImovelModel imovel;

    public FotoImovelModel() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    public Boolean getCapa() { return capa; }
    public void setCapa(Boolean capa) { this.capa = capa; }
    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
    public ImovelModel getImovel() { return imovel; }
    public void setImovel(ImovelModel imovel) { this.imovel = imovel; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof FotoImovelModel foto)) return false;
        return Objects.equals(id, foto.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
