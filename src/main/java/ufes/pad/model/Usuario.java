package ufes.pad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Usuario implements Serializable, UserDetails{
	/** Serialization id. */
	private static final long serialVersionUID = 1L;	

	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	@Column(length=150, nullable = false)
	private String nome;
	
	@Column(length=50, nullable = false)
	private String email;
	
	@Column(length=25, nullable = false)
	private String nome_usuario;	
	
	private String senha;
	
	private boolean apto = false;
	
	private String papel = "ROLE_USER";

	public boolean isApto() {
		return apto;
	}

	public void setApto(boolean apto) {
		this.apto = apto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}	

	public String getNome_usuario() {
		return nome_usuario;
	}

	public void setNome_usuario(String nome_usuario) {
		this.nome_usuario = nome_usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	// ################################# Methods #######################################	
	static public void printUsuario (Usuario user){
		System.out.println("---- Usuario ----");
		System.out.println("ID: " + user.id);
		System.out.println("Nome: " + user.nome);
		System.out.println("Nome Usuario: " + user.nome_usuario);
		System.out.println("Email: " + user.email);
		System.out.println("Apto: " + user.apto);		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		List<Roles> papeis = new ArrayList<Roles>();
		papeis.add(new Roles(this.papel));
		
		for (Roles p: papeis) {
			System.out.println(p.getAuthority());
		}
		
		
		return papeis;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.nome_usuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}	
}

class Roles implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String papel = "ROLE_USER";
	
	Roles (String p){
		this.setPapel(p);
	}
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.getPapel();
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}
	
}
