package ndr.brt.sieve.acceptance.pojo;

import java.util.List;

public class Town {
    private final String name;
    private final List<District> districts;
    private final Person major;

    public Town(String name, Person major, List<District> districts) {
        this.name = name;
        this.districts = districts;
        this.major = major;
    }

    public List<District> getDistricts() {
        return districts;
    }
}
