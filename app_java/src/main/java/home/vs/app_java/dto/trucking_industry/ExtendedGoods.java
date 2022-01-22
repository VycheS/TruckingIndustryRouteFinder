package home.vs.app_java.dto.trucking_industry;

import home.vs.app_java.dto.CoordinateDTO;

public class ExtendedGoods extends Goods {
    private CoordinateDTO deliveryPointCoordinate;
    public ExtendedGoods(Goods goods, CoordinateDTO deliveryPointCoordinate) {
        super(goods.getId(), goods.getName(), goods.getWeight(), goods.getDeliveryPointId(), goods.getCoordinate());
        this.deliveryPointCoordinate = deliveryPointCoordinate;
    }
    public CoordinateDTO getDeliveryPointCoordinate() {
        return deliveryPointCoordinate;
    }
    public void setDeliveryPointCoordinate(CoordinateDTO deliveryPointCoordinate) {
        this.deliveryPointCoordinate = deliveryPointCoordinate;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deliveryPointCoordinate == null) ? 0 : deliveryPointCoordinate.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExtendedGoods other = (ExtendedGoods) obj;
        if (deliveryPointCoordinate == null) {
            if (other.deliveryPointCoordinate != null)
                return false;
        } else if (!deliveryPointCoordinate.equals(other.deliveryPointCoordinate))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ExtendedGoods [deliveryPointCoordinate=" + deliveryPointCoordinate + "]";
    }
}
