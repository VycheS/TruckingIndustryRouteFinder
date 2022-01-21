package home.vs.app_java.service.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import home.vs.app_java.service.OsrmInteractionService;
import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.osrm.*;

@Service
public class OsrmInteractionServiceImpl implements OsrmInteractionService {
    private final RestTemplate restTemplate;
    private final String URL;
    private final Map<String,String> uriVariables;//alternatives={true|false|number}&steps={true|false}&geometries={polyline|polyline6|geojson}&overview={full|simplified|false}&annotations={true|false}
    
    public OsrmInteractionServiceImpl(String url, Map<String, String> uriVariables) {
        this.restTemplate = new RestTemplate();
        URL = url;
        this.uriVariables = uriVariables;
    }
    public OsrmInteractionServiceImpl() {
        this("http://osrm:5000/route/v1/driving/", new HashMap<>());
        this.uriVariables.put("alternatives", "false");
        this.uriVariables.put("geometries", "geojson");
        this.uriVariables.put("steps", "false");
        this.uriVariables.put("annotations", "false");
        this.uriVariables.put("overview", "simplified");
    }
    @Override
    public Double getLengthOfRoute(List<CoordinateDTO> listOfCoordinates) {
        if (listOfCoordinates != null && listOfCoordinates.size() > 1) {
            this.uriVariables.put("overview", "false");
            RouterContainer routerContainer = getRouterContainer(listOfCoordinates);
            List<Route> listOfRoutes = routerContainer.getRoutes();
            Route route = listOfRoutes.get(0);
            String strDistance = route.getDistance();
            return Double.parseDouble(strDistance);
        }
        return null;
    }
    @Override
    public List<CoordinateDTO> getRoute(List<CoordinateDTO> listOfCoordinates) {
        if (listOfCoordinates != null && listOfCoordinates.size() > 1) {
            this.uriVariables.put("overview", "full");
            RouterContainer routerContainer = getRouterContainer(listOfCoordinates);
            List<Route> listOfRoutes = routerContainer.getRoutes();
            Route route = listOfRoutes.get(0);
            Geometry geometry = route.getGeometry();
            List<List<Double>> listOfArrCoordinates = geometry.getCoordinates();
            List<CoordinateDTO> coordinates = new ArrayList<>(listOfArrCoordinates.size());
            for (List<Double> arrCoordinates : listOfArrCoordinates) {
                // coordinates.add(new CoordinateDTO(arrCoordinates.get(0), arrCoordinates.get(1)));
                coordinates.add(new CoordinateDTO(arrCoordinates.get(1), arrCoordinates.get(0)));
            }
            return coordinates;
        }
        return null;
    }

    private RouterContainer getRouterContainer(List<CoordinateDTO> listOfCoordinates) {
        List<String> listOfStrCoordinates = new ArrayList<>();
        for (CoordinateDTO coordinate : listOfCoordinates) {
            listOfStrCoordinates.add(String.format("%s,%s", coordinate.getLongitude(), coordinate.getLatitude()));
        }
        final String paramsTemplate = "?alternatives={alternatives}&steps={steps}&geometries={geometries}&overview={overview}&annotations={annotations}";
        
        return restTemplate.getForObject(URL + String.join(";", listOfStrCoordinates) + paramsTemplate, RouterContainer.class, uriVariables);
    }
}
