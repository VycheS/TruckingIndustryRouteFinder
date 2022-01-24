package home.vs.app_java.service;

import java.util.List;

import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.trucking_industry.TruckRoute;

public interface OsrmInteractionService {

    public Double getLengthOfRoute(List<CoordinateDTO> listOfCoordinate);
    
    public TruckRoute getRoute(List<CoordinateDTO> listOfCoordinate);
}
