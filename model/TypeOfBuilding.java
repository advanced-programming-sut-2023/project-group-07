
package model;

import java.util.ArrayList;

import javax.tools.Diagnostic.Kind;

public enum TypeOfBuilding {
    LITTLE_STONE_GATE(1000,5,5,0),
    LARGE_STONE_GATE(2000,7,7,0),
    DRAW_BRIDGE(-1,5,5,0),
    LOOK_OUT_TOWER(250,3,3,0),
    PERIMETER_TURRENT(1000,4,4,0),
    DEFENCE_TURRENT(1200,5,5,0),
    SQUARE_TOWER(1600,6,6,0),
    ROUND_TOWER(2000,6,6,0),
    ARMOURY(500,4,4,0),
    MERCENARY_POST(400,10,10,0),
    BARRACKS(500,10,10,0),
    ENGINEERS_GUILD(400,5,10,0),
    KILLING_PIT(-1,1,1,0),
    INN(400,6,6,1),
    MILL(200,3,3,3),
    IRON_MINE(300,4,4,2),
    OX_TETHER(100,2,2,1),
    QUARRY(350,6,6,3),
    MARKET(200,5,5,0),
    PITCH_RIG(200,4,4,2),
    STOCK(-1,5,5,0),
    WOODCUTTER(150,3,3,1),
    HOVEL(200,4,4,0),
    CHURCH(600,9,9,1),
    CATHEDRAL(1200,13,13,1),
    ARMOURER(500,4,4,1),
    BLACKSMITH(500,4,4,1),
    POLETURNER(500,4,4,1),
    FLETCHER(500,4,4,1),
    TANNER(500,4,4,1),
    OIL_SMELTER(-1,1,1,0),
    PITCH_DITCH(300,4,8,1),
    CAGED_WAR_DOGS(-1,2,2,0),
    SIEGE_TENT(100,3,3,0),
    STABLE(300,6,6,1),
    APPLE_ORCHARD(200,9,9,1),
    DIARY_FARMER(200,9,9,1),
    WHEAT_FARMER(200,9,9,1),
    HOPS_FARMER(200,9,9,1),
    HUNTER_POST(200,9,9,1),
    BAKERY(300,4,4,1),
    BREWER(300,4,4,1),
    GRANARY(250,4,4,1);

    private int hp;
    private int length;
    private int width;
    private int workerInUse;

    private TypeOfBuilding(int hp, int length, int width, int workerInUse){
        this.hp=hp;
        this.length=length;
        this.width=width;
        this.workerInUse=workerInUse;
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
    public TypeOfBuilding getBuilding(String input){ // todo : make this for
        switch (input){
            case "little stone gate":
                return LITTLE_STONE_GATE;
            case "large stone gate":
                return LARGE_STONE_GATE;
            case "draw bridge":
                return DRAW_BRIDGE;
            case "look out tower":
                return LOOK_OUT_TOWER;
            case "perimeter turrent":
                return PERIMETER_TURRENT;
            case "defence turrent":
                return DEFENCE_TURRENT;
            case "square tower":
                return SQUARE_TOWER;
            case "round tower":
                return ROUND_TOWER;
            case "armoury":
                return ARMOURY;
            case "mercenary post":
                return MERCENARY_POST;
            case "barracks":
                return BARRACKS;
            case "engineers guild":
                return ENGINEERS_GUILD;
            case "killing pit":
                return KILLING_PIT;
            case "inn":
                return INN;
            case "mill":
                return MILL;
            case "iron mine":
                return IRON_MINE;
            case "ox tether":
                return OX_TETHER;
            case "quarry":
                return QUARRY;
            case "market":
                return MARKET;
            case "pitch rig":
                return PITCH_RIG;
            case "stock":
                return STOCK;
            case "woodcutter":
                return WOODCUTTER;
            case "hovel":
                return HOVEL;
            case "church":
                return CHURCH;
            case "cathedral":
                return CATHEDRAL;
            case "armourer":
                return ARMOURER;
            case "blacksmith":
                return BLACKSMITH;
            case "poleturner":
                return POLETURNER;
            case "fletcher":
                return FLETCHER;
            case "tanner":
                return TANNER;
            case "oil smelter":
                return OIL_SMELTER;
            case "pitch ditch":
                return PITCH_DITCH;
            case "caged war dogs":
                return CAGED_WAR_DOGS;
            case "siege tent":
                return SIEGE_TENT;
            case "stable":
                return STABLE;
            case "apple orchard":
                return APPLE_ORCHARD;
            case "diary farmer":
                return DIARY_FARMER;
            case "wheat farmer":
                return WHEAT_FARMER;
            case "hops farmer":
                return HOPS_FARMER;
            case "hunter post":
                return HUNTER_POST;
            case "bakery":
                return BAKERY;
            case "brewer":
                return BREWER;
            case "granary":
                return GRANARY;
            default:
                return null;
        }
    }

package controller;

public class TypeOfBuilding() {
// todo
}