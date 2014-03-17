/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.bo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 *
 * @author mido
 */
@Stateless
public class PassengerFacade extends AbstractFacade<Passenger> {

    @PersistenceContext(unitName = "taxi360-ejbPU")
    private EntityManager em;

    /**
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     *
     */
    public PassengerFacade() {
        super(Passenger.class);
    }

    /**
     *
     * @param p
     * @return
     */
    public Passenger findByEmail(Passenger p) {
        try {
            TypedQuery<Passenger> tq = em.createNamedQuery("Passenger.findbyemail", Passenger.class);
            tq.setParameter("emailid", p.getEmailid());
            tq.setParameter("registrationtype", p.getRegistrationtype());
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param p
     */
    public void updateAccessKey(Passenger p) {
        TypedQuery<Object> tq = em.createNamedQuery("Passenger.updateAccessKey", Object.class);
        tq.setParameter("accesskey", p.getAccesskey());
        tq.setParameter("id", p.getId());
        tq.executeUpdate();
    }

    /**
     *
     * @param pass
     * @return
     */
    public Long createPassenger(Passenger pass) {
        create(pass);
        em.flush();
        return pass.getId();
    }
    
        /**
     *
     * @param p
     * @return
     */
    public Passenger validatePassword(Passenger p) {
        try {
            TypedQuery<Passenger> tq = em.createNamedQuery("Passenger.validatePassword", Passenger.class);
            tq.setParameter("emailid", p.getEmailid());
            tq.setParameter("registrationtype", p.getRegistrationtype());
            tq.setParameter("password", p.getPassword());
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Passenger validatePassword(String email, String password) {
        try {
            TypedQuery<Passenger> tq = em.createNamedQuery("Passenger.validatePassword", Passenger.class);
            tq.setParameter("emailid", email);
            tq.setParameter("password", password);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        
    }
}
