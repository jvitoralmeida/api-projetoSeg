package br.com.joaoalmeida.login.bussines;

import java.net.URI;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.mail.Fila;
import br.com.joaoalmeida.login.repository.UserRepository;
import br.com.joaoalmeida.login.utils.Constantes;
import br.com.joaovitor.Mail;

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

	public ResponseEntity<?> enviaSenhaEmail(User user) {
		final User userSearched = userRepository.findByUsername(user.getUsername());

		if (userSearched.getEmail() == null || userSearched == null) {
			return ResponseEntity.badRequest().build();
		}

		final Mail mail = new Mail.ConstroiEmail().emailRemetente(Constantes.EMAIL).senhaRemetente(Constantes.SENHA)
				.destinatario(userSearched.getEmail()).tituloEmail("RECUPERAÇÃO DE SENHA")
				.conteudoEmail("<h1>Você solicitou a recuperação de sua senha!!!</h1>" + "<h3><br> Username: "
						+ userSearched.getUsername() + "<br> Senha: " + userSearched.getPassword()
						+ "</h3><br><h2> Se você não reconhece essa solicitação, comunique o suporte!!!<h2>")
				.build();

		Fila.addInQueue(mail);

		return ResponseEntity.ok(User.buildDTO(userSearched));

	}
}
