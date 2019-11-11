package br.com.joaoalmeida.login.bussines;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.repository.UserRepository;
import br.com.joaoalmeida.login.utils.Constantes;
import br.com.joaoalmeida.login.utils.TokenUtils;
import br.com.joaovitor.Mail;

@Service
public class UserBusiness {

	@Autowired
	UserRepository userRepository;

	public ResponseEntity<User> validarLogin(User user) {

		final User usuarioCripto = usuarioCripto(user);
		final User findUser = userRepository.findUser(usuarioCripto.getUsername(), usuarioCripto.getPassword());

		if (Objects.nonNull(findUser)) {
			return ResponseEntity.ok(findUser);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<User> criarLogin(User user) {

		final User userCripto = usuarioCripto(user);
		final User userSaved = userRepository.save(userCripto);

		return ResponseEntity.created(buildUri(userSaved.getId())).body(userSaved);
	}

	private URI buildUri(String id) {
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/users/login/{id}").buildAndExpand(id)
				.toUri();

		return uri;
	}

	public ResponseEntity<?> enviaSenhaEmail(User user) {
		Integer token = null;
		Boolean verificaExitenciaToken = false;
		final User userSearched = userRepository.findByUsername(user.getUsername());

		if (userSearched == null || userSearched.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}

		do {
			token = new Random().nextInt(5000 - 1000 + 1) + 1000;
			verificaExitenciaToken = TokenUtils.getInstance().verificaExitenciaToken(token);
		} while (verificaExitenciaToken);

		TokenUtils.getInstance().setToken(token, userSearched.getUsername());

		final Mail mail = new Mail.ConstroiEmail().emailRemetente(Constantes.EMAIL).senhaRemetente(Constantes.SENHA)
				.destinatario(userSearched.getEmail()).tituloEmail("RECUPERAÇÃO DE SENHA")
				.conteudoEmail("<h1>Você solicitou a recuperação de sua senha!!!</h1>"
						+ "<br><h3><a href=\'http://localhost:3000/trocarSenha\'>Clique aqui para alterar sua senha</a></h3>"
						+ "<br><h2>Seu token: " + token
						+ "</h2><br><h2> Se você não reconhece essa solicitação, comunique o suporte!!!<h2>")
				.build();

		final Thread t = new Thread(() -> mail.enviaEmail());

		t.start();

		// Fila.addInQueue(mail);

		return ResponseEntity.ok(User.buildDTO(userSearched));

	}

	private User usuarioCripto(User user) {

		final String senha = user.getPassword();
		final byte[] messageDigest = criptografarSenha(senha);
		user.setPassword(messageDigest.toString());

		return user;
	}

	private byte[] criptografarSenha(final String senha) {
		MessageDigest algorith = null;
		byte messageDigest[] = null;
		try {
			algorith = MessageDigest.getInstance("SHA-256");
			messageDigest = algorith.digest(senha.getBytes("UTF-8"));
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return messageDigest;
	}

	public ResponseEntity<?> trocaSenha(String dadosUser) {

		final JsonObject objTroca = new Gson().fromJson(dadosUser, JsonObject.class);

		final Integer token = objTroca.get("token").getAsInt();
		final String novaSenha = objTroca.get("senha").getAsString();

		final String usernameToken = TokenUtils.getInstance().validaToken(token);

		if(buildQueryChangePass(novaSenha, usernameToken)) return ResponseEntity.ok().build(); 
		return ResponseEntity.badRequest().build();
	}

	private Boolean buildQueryChangePass(final String novaSenha, final String usernameToken) {
		
		final HttpClient httpClient = HttpClients.createDefault();
		final HttpPost httpPost = new HttpPost("http://localhost:9200/users/login/_update_by_query");
		httpPost.addHeader("Content-Type", "application/json");

		final StringBuilder sb = new StringBuilder();
		sb.append("{\"script\":{\"source\":\"ctx._source.password='");
		sb.append(criptografarSenha(novaSenha.toString()));
		sb.append("'\",\"lang\":\"painless\"},\"query\":{\"bool\":{\"must\":{\"match\":{\"username\":\"");
		sb.append(usernameToken);
		sb.append("\"}}}}}");

		StringEntity entity;

		if (usernameToken != null) {
			try {
				entity = new StringEntity(sb.toString());
				httpPost.setEntity(entity);
				final HttpResponse execute = httpClient.execute(httpPost);
				final HttpEntity entity1 = execute.getEntity();
				final String responseString = EntityUtils.toString(entity1, "UTF-8");
				System.out.println(responseString);
				return true;

			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (final ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public void request() {

	}
}
