package com.example.demo.dtos;

import com.example.demo.models.TipoUsuario;
import com.example.demo.models.UserModel;

/**
 * DTO (Data Transfer Object) do usuario.
 *
 * Serve para transferir dados entre as camadas sem expor a entidade do banco.
 * Repare que NAO existe o campo `senha`: e justamente por isso que a senha
 * deixa de aparecer nas respostas da API.
 *
 * Getters e setters escritos a mao (o Lombok nao roda no JDK 26 deste ambiente).
 */
public class UserDTO {

    private Integer id;
    private String nome;
    private String email;
    private TipoUsuario tipo;

    public UserDTO() {
    }

    /**
     * Converte a entidade em DTO, copiando apenas os campos publicos.
     */
    public UserDTO(UserModel userModel) {
        this.id = userModel.getId();
        this.nome = userModel.getNome();
        this.email = userModel.getEmail();
        this.tipo = userModel.getTipo();
    }

    public UserDTO(Integer id, String nome, String email, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
}
