package ndr.brt.sieve.acceptance.pojo;

public class House {
    private final String address;
    private final Integer people;
    private final Integer floors;
    private final Boolean hasBasement;

    public House(String address, Integer people, Integer floors, Boolean hasBasement) {
        this.address = address;
        this.people = people;
        this.floors = floors;
        this.hasBasement = hasBasement;
    }

    public boolean hasNoBasement() {
        return !hasBasement;
    }
}
