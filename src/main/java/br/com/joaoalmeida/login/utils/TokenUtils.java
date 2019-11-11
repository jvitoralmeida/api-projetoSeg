package br.com.joaoalmeida.login.utils;

import java.util.HashMap;

public class TokenUtils {

	private static TokenUtils instance;

	private final HashMap<Integer, String> token = new HashMap<Integer, String>();

	private TokenUtils() {
	}

	public static TokenUtils getInstance() {
		if (instance == null) {
			synchronized (TokenUtils.class) {
				if (instance == null) {
					instance = new TokenUtils();
				}
			}
		}
		return instance;
	}

	public void setToken(Integer token, String username) {
		this.token.put(token, username);
	}

	public String validaToken(Integer token) {
		if (this.token.containsKey(token)) {
			final String usernameToken = this.token.get(token);
			this.token.remove(token);
			return usernameToken;
		}
		return null;
	}

	public Boolean verificaExitenciaToken(Integer token) {
		return this.token.containsKey(token);
	}
}
