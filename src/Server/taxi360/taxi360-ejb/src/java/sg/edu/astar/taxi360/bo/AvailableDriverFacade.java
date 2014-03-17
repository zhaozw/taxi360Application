/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.bo;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import sg.edu.astar.taxi360.entity.AvailableDriver;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Passenger;
import sg.edu.astar.taxi360.entity.Request;

/**
 *
 * @author Althaf
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
    
    public AvailableDriver findById (  Long id ) {
        try {
        TypedQuery<AvailableDriver> tq = em.createNamedQuery("AvailableDriver.findById", AvailableDriver.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
        } catch(NoResultException e) {
        return null;
        }
    }
    
    public List<AvailableDriver> findWithin(Location location, Double radius) {
        String sql = "Select availabledriver.* from availabledriver where ST_DWithin(location, ST_GeographyFromText('SRID=4326;POINT( "+location.getLongitude()+" "+location.getLatitude()+")'),"+radius+")";
        Query query = em.createNativeQuery(sql, AvailableDriver.class);
        return query.getResultList();
    }
}
