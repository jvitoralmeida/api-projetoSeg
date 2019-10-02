package br.com.joaoalmeida.login.bussines;

import java.net.URI;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.repository.UserRepository;

@Service
public class UserBusiness {

	@Autowired
	UserRepository userRepository;

	public ResponseEntity<User> validarLogin(User user) {

		final User findUser = userRepository.findUser(user.getUsername(), user.getPassword());

		if (Objects.nonNull(findUser)) {
			return ResponseEntity.ok(findUser);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<User> criarLogin(User user) {
		final User userSaved = userRepository.save(user);

		return ResponseEntity.created(buildUri(userSaved.getId())).body(userSaved);
	}

	private URI buildUri(String id) {
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/users/login/{id}").buildAndExpand(id)
				.toUri();

		return uri;
	}
}
