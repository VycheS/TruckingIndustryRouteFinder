package home.vs.app_java.dto.trucking_industry;

import java.util.List;

public final class ListsOfTruckingIndustryEntities {
    private final List<Truck> listOfTrucks;
    private final List<Goods> listOfGoods;
    private final List<DeliveryPoint> listOfDeliveryPoints;
    public ListsOfTruckingIndustryEntities(List<Truck> listOfTrucks, List<Goods> listOfGoods,
            List<DeliveryPoint> listOfDeliveryPoints) {
        this.listOfTrucks = listOfTrucks;
        this.listOfGoods = listOfGoods;
        this.listOfDeliveryPoints = listOfDeliveryPoints;
    }
    public List<Truck> getListOfTrucks() {
        return listOfTrucks;
    }
    public List<Goods> getListOfGoods() {
        return listOfGoods;
    }
    public List<DeliveryPoint> getListOfDeliveryPoints() {
        return listOfDeliveryPoints;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((listOfDeliveryPoints == null) ? 0 : listOfDeliveryPoints.hashCode());
        result = prime * result + ((listOfGoods == null) ? 0 : listOfGoods.hashCode());
        result = prime * result + ((listOfTrucks == null) ? 0 : listOfTrucks.hashCode());
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
        ListsOfTruckingIndustryEntities other = (ListsOfTruckingIndustryEntities) obj;
        if (listOfDeliveryPoints == null) {
            if (other.listOfDeliveryPoints != null)
                return false;
        } else if (!listOfDeliveryPoints.equals(other.listOfDeliveryPoints))
            return false;
        if (listOfGoods == null) {
            if (other.listOfGoods != null)
                return false;
        } else if (!listOfGoods.equals(other.listOfGoods))
            return false;
        if (listOfTrucks == null) {
            if (other.listOfTrucks != null)
                return false;
        } else if (!listOfTrucks.equals(other.listOfTrucks))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ListsOfTruckingIndustryEntities [listOfDeliveryPoints=" + listOfDeliveryPoints + ", listOfGoods="
                + listOfGoods + ", listOfTrucks=" + listOfTrucks + "]";
    }
    
}
