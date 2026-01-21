package br.com.escola.dao;

import br.com.escola.model.Matricula;
import br.com.escola.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class MatriculaDAO {

    public void salvar(Matricula matricula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(matricula);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Matricula buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public List<Matricula> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Matricula> query = em.createQuery(
                "SELECT m FROM Matricula m JOIN FETCH m.aluno JOIN FETCH m.curso ORDER BY m.dataMatricula DESC", 
                Matricula.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Matricula> listarPorAluno(Long alunoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Matricula> query = em.createQuery(
                "SELECT m FROM Matricula m JOIN FETCH m.curso WHERE m.aluno.id = :alunoId", 
                Matricula.class);
            query.setParameter("alunoId", alunoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Matricula> listarPorCurso(Long cursoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Matricula> query = em.createQuery(
                "SELECT m FROM Matricula m JOIN FETCH m.aluno WHERE m.curso.id = :cursoId", 
                Matricula.class);
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Métodos para o Relatório de Engajamento (Bônus)
    public Long contarAlunosPorCurso(Long cursoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(m) FROM Matricula m WHERE m.curso.id = :cursoId", Long.class);
            query.setParameter("cursoId", cursoId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Double mediaIdadeAlunosPorCurso(Long cursoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(YEAR(CURRENT_DATE) - YEAR(m.aluno.dataNascimento)) " +
                "FROM Matricula m WHERE m.curso.id = :cursoId", Double.class);
            query.setParameter("cursoId", cursoId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Long contarMatriculasUltimos30Dias(Long cursoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDate dataLimite = LocalDate.now().minusDays(30);
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(m) FROM Matricula m WHERE m.curso.id = :cursoId AND m.dataMatricula >= :dataLimite", 
                Long.class);
            query.setParameter("cursoId", cursoId);
            query.setParameter("dataLimite", dataLimite);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Matricula matricula = em.find(Matricula.class, id);
            if (matricula != null) {
                em.remove(matricula);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
