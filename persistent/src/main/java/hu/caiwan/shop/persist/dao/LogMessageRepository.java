package hu.caiwan.shop.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.LogMessage;

public interface LogMessageRepository extends JpaRepository<LogMessage, Long>{

}
