package br.com.joaoalmeida.login.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.com.joaoalmeida.login.bussines.UserBusiness;
import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "UserController")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserBusiness userBussiness;

	@ApiOperation(value = "Adiciona usuário")
	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {

		return userBussiness.criarLogin(user);
	}

	@ApiOperation(value = "Retorna todos usuários")
	@GetMapping("/users/all")
	public List<User> getAllUsers() {
		final Iterator<User> iterator = userRepository.findAll().iterator();
		final List<User> users = new ArrayList<>();

		while (iterator.hasNext()) {
			users.add(iterator.next());
		}

		return users;
	}

	@ApiOperation(value = "Recuperação de senha")
	@PostMapping("/senhaEmail")
	public ResponseEntity<?> enviaSenhaEmail(@RequestBody User user) {

		return userBussiness.enviaSenhaEmail(user);
	}

	@ApiOperation(value = "Recupera usuário")
	@PostMapping("/user")
	public ResponseEntity<User> getUser(@RequestBody String userString) {

		final User user = new Gson().fromJson(userString, User.class);

		return userBussiness.validarLogin(user);
	}

	@ApiOperation(value = "Deleta usuário pelo ID")
	@DeleteMapping("/user/{id}")
	public ResponseEntity<User> deleteById(String id) {

		userRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/trocaSenha")
	public ResponseEntity<?> trocaSenha(@RequestBody String dadosUser) {

		userBussiness.trocaSenha(dadosUser);

		return null;
	}
}
