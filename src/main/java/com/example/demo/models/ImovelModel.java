package com.example.demo.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "imoveis")
public class ImovelModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "preco_venda", precision = 15, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "preco_aluguel", precision = 15, scale = 2)
    private BigDecimal precoAluguel;

    private String finalidade;
    private String status;
    private Integer dormitorios;
    private Integer banheiros;
    private Integer garagem;

    @Column(name = "area_total", precision = 15, scale = 2)
    private BigDecimal areaTotal;

    @Column(name = "area_construida", precision = 15, scale = 2)
    private BigDecimal areaConstruida;

    private String endereco;
    private String numero;
    private String complemento;
    private String cep;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    private Boolean destaque;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_imovel_id")
    private TipoImovelModel tipoImovel;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bairro_id")
    private BairroModel bairro;

    public ImovelModel() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public BigDecimal getPrecoAluguel() { return precoAluguel; }
    public void setPrecoAluguel(BigDecimal precoAluguel) { this.precoAluguel = precoAluguel; }
    public String getFinalidade() { return finalidade; }
    public void setFinalidade(String finalidade) { this.finalidade = finalidade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getDormitorios() { return dormitorios; }
    public void setDormitorios(Integer dormitorios) { this.dormitorios = dormitorios; }
    public Integer getBanheiros() { return banheiros; }
    public void setBanheiros(Integer banheiros) { this.banheiros = banheiros; }
    public Integer getGaragem() { return garagem; }
    public void setGaragem(Integer garagem) { this.garagem = garagem; }
    public BigDecimal getAreaTotal() { return areaTotal; }
    public void setAreaTotal(BigDecimal areaTotal) { this.areaTotal = areaTotal; }
    public BigDecimal getAreaConstruida() { return areaConstruida; }
    public void setAreaConstruida(BigDecimal areaConstruida) { this.areaConstruida = areaConstruida; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }
    public Boolean getDestaque() { return destaque; }
    public void setDestaque(Boolean destaque) { this.destaque = destaque; }
    public TipoImovelModel getTipoImovel() { return tipoImovel; }
    public void setTipoImovel(TipoImovelModel tipoImovel) { this.tipoImovel = tipoImovel; }
    public BairroModel getBairro() { return bairro; }
    public void setBairro(BairroModel bairro) { this.bairro = bairro; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ImovelModel imovel)) return false;
        return Objects.equals(id, imovel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
