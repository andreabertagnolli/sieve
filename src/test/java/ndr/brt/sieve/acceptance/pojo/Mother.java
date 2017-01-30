package ndr.brt.sieve.acceptance.pojo;

import java.util.ArrayList;
import java.util.List;

public class Mother extends Person {

    private List<Person> sons = new ArrayList<>();

    public Mother(String name, int age) {
        super(name, age);
    }

    public void addSon(Person son) {
        sons.add(son);
    }

    public List<Person> getSons() {
        return sons;
    }
}
