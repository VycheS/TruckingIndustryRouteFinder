package home.vs.app_java.service;

import java.util.List;

import home.vs.app_java.dto.trucking_industry.ListsOfTruckingIndustryEntities;
import home.vs.app_java.dto.trucking_industry.TruckRoute;

public interface RouteFindingAlgorithm {
    public List<TruckRoute> getRoutes (ListsOfTruckingIndustryEntities listsOfTruckingIndustryEntities);
}
