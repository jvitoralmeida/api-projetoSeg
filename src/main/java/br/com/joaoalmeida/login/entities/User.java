package br.com.joaoalmeida.login.entities;

import java.io.Serializable;

import org.elasticsearch.common.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import br.com.joaoalmeida.login.entities.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users", type = "login", createIndex = true)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Nullable
	private String id;
	private String nome;
	private String username;
	private String email;
	private String password;

	public static UserDTO buildDTO(User user) {
		final UserDTO userDto = new UserDTO(user.getId(), user.getNome(), user.getUsername(), user.getEmail());

		return userDto;
	}
}
