package home.vs.app_java.dto.osrm;

import java.util.List;

public class RouterContainer {
    private List<Route> routes;
    private List<Waypoint> waypoint;
    private String code;
    // public RouterContainer(List<Route> routes, List<Waypoint> waypoint, String code) {
    //     this.routes = routes;
    //     this.waypoint = waypoint;
    //     this.code = code;
    // }
    public List<Route> getRoutes() {
        return routes;
    }
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    public List<Waypoint> getWaypoint() {
        return waypoint;
    }
    public void setWaypoint(List<Waypoint> waypoint) {
        this.waypoint = waypoint;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((routes == null) ? 0 : routes.hashCode());
        result = prime * result + ((waypoint == null) ? 0 : waypoint.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RouterContainer other = (RouterContainer) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (routes == null) {
            if (other.routes != null)
                return false;
        } else if (!routes.equals(other.routes))
            return false;
        if (waypoint == null) {
            if (other.waypoint != null)
                return false;
        } else if (!waypoint.equals(other.waypoint))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "RouterContainer [code=" + code + ", routes=" + routes + ", waypoint=" + waypoint + "]";
    }
}
