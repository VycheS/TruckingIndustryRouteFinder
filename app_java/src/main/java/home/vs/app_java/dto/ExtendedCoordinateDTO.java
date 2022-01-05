package home.vs.app_java.dto;

public class ExtendedCoordinateDTO extends CoordinateDTO {
    private Integer id;

    public ExtendedCoordinateDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
