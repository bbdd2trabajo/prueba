package ar.edu.unlp.info.dssd.supermercado.jwt;

import java.util.Date;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class TokenManagerSecurity {

	/**
	 * semilla secreta
	 */
	
	private String secretKey = "&%$#!QWERT";

	
	/**
	 * <p>
	 * Tiempo de expiracion del token
	 * </p>
	 * <p>
	 * Se define en segundos
	 * </p>
	 */
	
	private Long ttlSeg = Long.valueOf(1 * 60 * 60 * 10);

	/**
	 * El mapper utilizado para pasar de un ServiceRequest a json y viceversa
	 */

	
	private ObjectMapper mapper = new ObjectMapper();

	public TokenManagerSecurity() {
		super();
	}

	/**
	 * Genera el token de autenticacion a partir de un usuario autenticado. El
	 * usuario se guarda como dato dentro del token. De esta forma se recibe en
	 * cada request.
	 * 
	 * @return token de autenticacion
	 * @throws Exception
	 */
	/**
	 * @param serviceRequest
	 * @return
	 * @throws Exception
	 */
	public String createJWT(String user) throws Exception {

		String subject = mapper.writeValueAsString(user);

		// The JWT signature algorithm we will be using to sign the token

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		//Antes
		System.out.println( subject.toString() );
		
		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).claim("content", subject).signWith(SignatureAlgorithm.HS512,
				secretKey);

		// if it has been specified, let's add the expiration
		if (getTtlSeg() >= 0) {
			long ttlMillis = getTtlSeg() * 1000;
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		
		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();

	}

	/**
	 * Parsea el token, verifica que sea valido, y finalmente devuelve el user.
	 * 
	 * @param jwt
	 *            token jwt
	 * @return El user si el token es valido y no expiro
	 * @throws ServiceException
	 */
	public String parseJWT(String jwt) {

		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
		return this.getContentJWT(claims.get("content").toString());
	}

	public String getContentJWT(String contentJson) {

		try {
			return mapper.readValue(contentJson, String.class);
		} catch (Exception e) {
			System.out.println("Error intentando parsear el payload del token: " + contentJson + e.getMessage());
			throw new IllegalStateException("Error de parseo. El payload del token no puede parsearse");
		}

	}

	public Long getTtlSeg() {
		return ttlSeg;
	}

	public void setTtlSeg(Long ttlSeg) {
		this.ttlSeg = ttlSeg;
	}

}

