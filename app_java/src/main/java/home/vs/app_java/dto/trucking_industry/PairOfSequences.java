package home.vs.app_java.dto.trucking_industry;

import java.util.LinkedList;
import java.util.List;

public class PairOfSequences {
    private final List<ExtendedGoods> goods;
    private final List<Truck> trucks;
    public PairOfSequences(List<ExtendedGoods> goods, List<Truck> trucks) {
        this.goods = goods;
        this.trucks = trucks;
    }
    public List<ExtendedGoods> getGoods() {
        return goods;
    }
    public List<Truck> getTrucks() {
        return trucks;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((goods == null) ? 0 : goods.hashCode());
        result = prime * result + ((trucks == null) ? 0 : trucks.hashCode());
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
        PairOfSequences other = (PairOfSequences) obj;
        if (goods == null) {
            if (other.goods != null)
                return false;
        } else if (!goods.equals(other.goods))
            return false;
        if (trucks == null) {
            if (other.trucks != null)
                return false;
        } else if (!trucks.equals(other.trucks))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "PairOfSequences [goods=" + goods + ", trucks=" + trucks + "]";
    }

    public PairOfSequences copy() {
        List<ExtendedGoods> listOfGoods = new LinkedList<>();
        for (ExtendedGoods extendedGoods : this.getGoods()) {
            listOfGoods.add(extendedGoods);
        }
        List<Truck> listOfTrucks = new LinkedList<>();
        for (Truck truck : this.getTrucks()) {
            listOfTrucks.add(truck);
        }
        return new PairOfSequences(listOfGoods, listOfTrucks);
    }
}
