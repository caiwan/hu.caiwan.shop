package hu.caiwan.shop.persist.dao;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.persist.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	
//	@Query("select t from VerificationToken t where token.valid=true and token=:token")
	VerificationToken findByToken(String token);
	
//	@Query("select t from VerificationToken t where token.valid=true and user=:user")
	VerificationToken findByUser(User user);
	
//	@Query("select t from VerificationToken t where token.valid=true and token=:token")
	Stream<VerificationToken> findByExpirationLessThan(Date expiration);
	
	void deleteByExpirationLessThan(Date now);
}
