package br.com.joaoalmeida.login.entities.builder;

import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.entities.DTO.UserDTO;

public class UserBuilder {

	public static UserDTO build(User user) {

		final UserDTO target = new UserDTO();
		target.setId(user.getId() == null ? null : user.getId());
		target.setUsername(user.getUsername() == null ? null : user.getUsername());
		target.setEmail(user.getEmail() == null ? null : user.getEmail());

		return target;
	}
}
