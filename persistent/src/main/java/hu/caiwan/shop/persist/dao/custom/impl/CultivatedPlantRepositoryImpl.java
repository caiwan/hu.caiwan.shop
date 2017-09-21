//package hu.caiwan.shop.persist.dao.custom.impl;
//
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.springframework.transaction.annotation.Transactional;
//
//import hu.caiwan.shop.persist.dao.custom.CustomCultivatedPlantRepository;
//import hu.caiwan.shop.persist.model.CultivatedPlant;
//
///**
// * Add some more unsupported functions to JPA repositories such as detach and merge. 
// * Has to be tied to JPA repository due to have the right persistence context.
// * @author caiwan
// *
// */
//
//@Transactional
//public class CultivatedPlantRepositoryImpl implements CustomCultivatedPlantRepository {
//@PersistenceContext	
//	private EntityManager em;
//	
//	public void merge(CultivatedPlant e){
//		em.merge(e);
//	}
//	
//	public void detach(CultivatedPlant e){
//		em.detach(e);
//	}
//
//	// http://stackoverflow.com/questions/14160402/merging-entity-with-a-unique-constraint
//	// http://stackoverflow.com/questions/803166/jpa-entitymanger-preforming-a-database-insert-on-merge
//	@Override
//	public void mergeUnique(CultivatedPlant e) {
//		if (e.getName().isEmpty()) 
//			return;
//		try {
//			Query q = em.createNamedQuery("CultivatedPlant.findByNameCustom");
//			q.setParameter("name", e.getName());
//			CultivatedPlant r = (CultivatedPlant) q.getSingleResult();
//			e.setID(r.getID());
//			em.merge(e);
//		} catch (NoResultException ex){
//			em.persist(e);
//		}
//	}
//	
//}
