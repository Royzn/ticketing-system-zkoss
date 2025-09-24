package latihan.repository.impl;

import latihan.entity.Ticket;
import latihan.repository.TicketRepository;
import latihan.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public List<Ticket> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Ticket t", Ticket.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Ticket ticket = em.find(Ticket.class, id);
            return Optional.ofNullable(ticket);
        } finally {
            em.close();
        }
    }

    @Override
    public Ticket save(Ticket ticket) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (ticket.getId() == null) {
                em.persist(ticket);
                em.flush();
            } else {
                ticket = em.merge(ticket);
            }
            tx.commit();
            return ticket;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ticket ticket = em.find(Ticket.class, id);
            if (ticket != null) {
                em.remove(ticket);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}