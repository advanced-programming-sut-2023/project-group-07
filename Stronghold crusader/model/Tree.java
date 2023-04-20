package model;

public enum Tree {
    DATE_PALM ("date palm"),
    COCONUT_PALM ("coconut palm"),
    CHERRY_PALM ("cherry palm"),
    OLIVE_TREE ("olive tree"),
    DESERT_SHRUB ("desert shrub");

    private final String name;
    private Tree(String name) {
        this.name = name;
    }

    public static Tree getTree(String treeName) {
        for(Tree tree : Tree.values())
            if(tree.name.equals(treeName))
                return tree;
        return null;
    }

    
}