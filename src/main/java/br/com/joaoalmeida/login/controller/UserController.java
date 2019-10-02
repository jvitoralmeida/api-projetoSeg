package br.com.joaoalmeida.login.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.joaoalmeida.login.bussines.UserBusiness;
import br.com.joaoalmeida.login.entities.User;
import br.com.joaoalmeida.login.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserBusiness userBussiness;

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {

		return userBussiness.criarLogin(user);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/users/all")
	public List<User> getAllUsers() {
		final Iterator<User> iterator = userRepository.findAll().iterator();
		final List<User> users = new ArrayList<>();

		while (iterator.hasNext()) {
			users.add(iterator.next());
		}

		return users;
	}

	@GetMapping("/user")
	public ResponseEntity<User> getUser(@RequestBody User user) {

		return userBussiness.validarLogin(user);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<User> deleteById(String id) {

		userRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}
}
