package br.com.joaoalmeida.login.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private String id;
	private String nome;
	private String username;
	private String email;
}
