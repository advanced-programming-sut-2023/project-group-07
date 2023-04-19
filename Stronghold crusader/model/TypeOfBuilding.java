
package model;

public enum TypeOfBuilding {
    LITTLE_STONE_GATE(1000,5,5,0,"little stone gate",null),
    LARGE_STONE_GATE(2000,7,7,0,"large stone gate",null),
    DRAW_BRIDGE(-1,5,5,0,"draw bridge",null),
    LOOK_OUT_TOWER(250,3,3,0,"look out tower",null),
    PERIMETER_TURRENT(1000,4,4,0,"perimeter turret",null),
    DEFENCE_TURRENT(1200,5,5,0,"defence turret",null),
    SQUARE_TOWER(1600,6,6,0,"square tower",null),
    ROUND_TOWER(2000,6,6,0,"round towernull",null),
    ARMOURY(500,4,4,0,"armoury",null),
    MERCENARY_POST(400,10,10,0,"mercenary post",null),
    BARRACKS(500,10,10,0,"barracks",null),
    ENGINEERS_GUILD(400,5,10,0,"engineer's guild",null),
    KILLING_PIT(-1,1,1,0,"killing pit",null),
    INN(400,6,6,1,"inn",null),
    MILL(200,3,3,3,"mill",null),
    IRON_MINE(300,4,4,2,"iron mine",Texture.IRON),
    OX_TETHER(100,2,2,1,"ox tether",null),
    QUARRY(350,6,6,3,"quarry",Texture.STONE),
    MARKET(200,5,5,0,"market",null),
    PITCH_RIG(200,4,4,2,"pitch rig",Texture.OIL),
    STOCK(-1,5,5,0,"stock",null),
    WOODCUTTER(150,3,3,1,"woodcutter",null),
    HOVEL(200,4,4,0,"hovel",null),
    CHURCH(600,9,9,1,"church",null),
    CATHEDRAL(1200,13,13,1,"cathedral",null),
    ARMOURER(500,4,4,1,"armourer",null),
    BLACKSMITH(500,4,4,1,"blacksmith",null),
    POLETURNER(500,4,4,1,"poleturner",null),
    FLETCHER(500,4,4,1,"fletcher",null),
    TANNER(500,4,4,1,"tanner",null),
    OIL_SMELTER(-1,1,1,0,"oil smelter",null),
    PITCH_DITCH(300,4,8,1,"pitch ditch",null),
    CAGED_WAR_DOGS(-1,2,2,0,"caged war dogs",null),
    SIEGE_TENT(100,3,3,0,"siege tent",null),
    STABLE(300,6,6,1,"stable",null),
    APPLE_ORCHARD(200,9,9,1,"apple orchard",Texture.MEADOW),
    DIARY_FARMER(200,9,9,1,"diary farmer",Texture.MEADOW),
    WHEAT_FARMER(200,9,9,1,"wheat farmer",Texture.MEADOW),
    HOPS_FARMER(200,9,9,1,"hops farmer",Texture.MEADOW),
    HUNTER_POST(200,9,9,1,"hunter post",null),
    BAKERY(300,4,4,1,"bakery",null),
    BREWER(300,4,4,1,"brewer",null),
    GRANARY(250,4,4,1,"granary",null);

    private int hp;
    private int length;
    private int width;
    private int workerInUse;
    private String buildingName;
    private Texture texture;


    private TypeOfBuilding(int hp, int length, int width, int workerInUse,String buildingName,Texture texture) {
        this.hp=hp;
        this.length=length;
        this.width=width;
        this.workerInUse=workerInUse;
        this.buildingName=buildingName;
    }
    public int getHp() {
        return hp;
    }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public Texture getTexture() {
        return texture;
    }
    public static TypeOfBuilding getBuilding(String input){ // todo : make this for
        for(TypeOfBuilding typeOfBuilding: TypeOfBuilding.values()){
            if(typeOfBuilding.buildingName.equals(input)) return typeOfBuilding;
        }
        return null;
    }

}