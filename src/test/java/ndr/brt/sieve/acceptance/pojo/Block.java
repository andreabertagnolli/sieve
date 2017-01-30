package ndr.brt.sieve.acceptance.pojo;

import java.util.List;

public class Block {
    private final Double area;
    private final List<House> houses;

    public Block(Double area, List<House> houses) {
        this.houses = houses;
        this.area = area;
    }

    public List<House> getHouses() {
        return houses;
    }
}
