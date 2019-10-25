package br.com.joaoalmeida.login.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import br.com.joaoalmeida.login.entities.User;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

	@Query("{\"bool\":{\"must\":[{\"match\":{\"username\":\"?0\"}},{\"match\":{\"password\":\"?1\"}}]}}")
	User findUser(String username, String password);

	@Query("{\"bool\":{\"must\":[{\"match\":{\"username\":\"?0\"}}]}}")
	User findByUsername(String username);
}
