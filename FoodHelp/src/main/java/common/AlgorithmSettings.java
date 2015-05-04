/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import dataModel.SearchAlgorithm;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import services.contracts.FoodRecommendationsService;
import services.*;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class AlgorithmSettings {
    private SearchAlgorithm searchAlgorithm = SearchAlgorithm.GENETIC;

    @Lock(LockType.READ)
    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    @Lock(LockType.WRITE)
    public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }
    
    public AlgorithmSettings() {}
    
    public FoodRecommendationsService getServiceInstance() {
        switch (getSearchAlgorithm()) {
            case GREEDY:
                return new GreedyAlgorithmService();
            case GENETIC:
                return new GeneticAlgorithmService();
            case BRUTEFORCE:
                return new BruteForceAlgorithmService();
        }
        return null;
    }
}
