package home.vs.app_java.service;

import java.util.List;

import home.vs.app_java.dto.CoordinateDTO;

public interface OsrmInteractionService {

    public Double getLengthOfRoute(List<CoordinateDTO> listOfCoordinate);
    
    public List<CoordinateDTO> getRoute(List<CoordinateDTO> listOfCoordinate);
}
