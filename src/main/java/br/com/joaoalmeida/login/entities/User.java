package br.com.joaoalmeida.login.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users", type = "login", createIndex = true)
public class User {

	@Id
	private String id;
	private String username;
	private String password;
}
