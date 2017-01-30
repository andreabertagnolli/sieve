package ndr.brt.sieve.acceptance.pojo;

import java.util.List;

public class District {
    private final String name;
    private final List<Block> blocks;
    private final Person president;

    public District(String name, Person president, List<Block> blocks) {
        this.name = name;
        this.blocks = blocks;
        this.president = president;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
