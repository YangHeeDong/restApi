package com.example.restApi;

import com.example.restApi.global.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
class RestApiApplicationTests {

	@Value("${custom.jwt.secretKey")
	private String  originSecretKey;

	@Autowired
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("시크릿 키 체크")
	void checkSecretKey () {
		assertThat(originSecretKey).isNotNull();
	}

	@Test
	@DisplayName("기존 시크릿 키를 암호화 알고리즘을 톻해 secretKey 객체 확인")
	void test2 () {

		String keyBase64Encoded = Base64.getEncoder().encodeToString(originSecretKey.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("JwtProvider 활용 secretKey 생성")
	void test3 (){

		assertThat(jwtProvider.getSecretKey()).isNotNull();

	}

	@Test
	@DisplayName("JwtProvider 객체 생성 1번만")
	void test4 (){

		SecretKey secretKey1 = jwtProvider.getSecretKey();
		SecretKey secretKey2 = jwtProvider.getSecretKey();

		assertThat(secretKey1 == secretKey2).isTrue();

	}

	@Test
	@DisplayName("accessToken 생성")
	void test5 (){

		Map<String, Object> claims = new HashMap<>();

		claims.put("id",30L);
		claims.put("username","홍길동");

		String accessToken = jwtProvider.genToken(claims,60);

		assertThat(accessToken).isNotNull();
	}

	@Test
	@DisplayName("만료되어 유효한지")
	void test6 (){

		Map<String, Object> claims = new HashMap<>();

		claims.put("id",30L);
		claims.put("username","홍길동");

		String accessToken = jwtProvider.genToken(claims,-1);

		assertThat(jwtProvider.verify(accessToken)).isFalse();
	}

	@Test
	@DisplayName("Claim 정보 가져오기")
	void test7 () {

		Map<String, Object> claims = new HashMap<>();

		claims.put("id",30L);
		claims.put("username","홍길동");

		String accessToken = jwtProvider.genToken(claims,60*60*24*365);

		assertThat(jwtProvider.verify(accessToken)).isTrue();

		Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);

	}


}
