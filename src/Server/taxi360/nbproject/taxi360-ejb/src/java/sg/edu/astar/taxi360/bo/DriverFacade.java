/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.bo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sg.edu.astar.taxi360.entity.Driver;

/**
 *
 * @author mido
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
    
}
