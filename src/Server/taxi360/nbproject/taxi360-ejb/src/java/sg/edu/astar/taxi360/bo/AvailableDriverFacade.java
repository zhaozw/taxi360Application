/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.bo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sg.edu.astar.taxi360.entity.AvailableDriver;

/**
 *
 * @author mido
 */
@Stateless
public class AvailableDriverFacade extends AbstractFacade<AvailableDriver> {
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
    public AvailableDriverFacade() {
        super(AvailableDriver.class);
    }
    
}
