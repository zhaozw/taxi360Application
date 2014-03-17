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
import sg.edu.astar.taxi360.entity.Driver;

/**
 *
 * @author Althaf
 */
@Stateless
public class DriverFacade extends AbstractFacade<Driver> {
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
    public DriverFacade() {
        super(Driver.class);
    }
    
            /**
     *
     * @param email
     * @param password
     * @return
     */
    public Driver validatePassword(String email, String password) {
        try {
            TypedQuery<Driver> tq = em.createNamedQuery("Driver.validatePassword", Driver.class);
            tq.setParameter("emailid", email);
            tq.setParameter("password", password);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public long createDriver(Driver driver) {
        create(driver);
        em.flush();
        return driver.getId();
    }
    
}
