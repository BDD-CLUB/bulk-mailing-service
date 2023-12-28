package io.springbatch.springbatch;

import io.springbatch.springbatch.entity.MemberRepository;
import io.springbatch.springbatch.service.SignUpService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class IntegrationTest {

	@Autowired
	protected MemberRepository memberRepository;

	@SpyBean
	protected SignUpService signUpService;

	@PersistenceContext
	protected EntityManager em;

}
