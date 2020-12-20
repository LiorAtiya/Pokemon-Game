package test;

import api.geo_location;
import gameClient.Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    public static Pokemon createPokemon(){
        Point3D p = new Point3D(1,2,3);
        Pokemon pok = new Pokemon(p,-1,20,null);
        return pok;
    }

    @Test
    void IDTest() {
        Pokemon p = createPokemon();
        p.setID(10);
        assertEquals(p.getID(),10);
    }

    @Test
    void edgeTest() {
        Pokemon p = createPokemon();
        assertNull(p.get_edge());
    }


    @Test
    void getLocation() {
        Pokemon p = createPokemon();
        assertEquals(1,p.getLocation().x());
        assertEquals(2,p.getLocation().y());
        assertEquals(3,p.getLocation().z());
    }

    @Test
    void getType() {
        Pokemon p = createPokemon();
        assertEquals(-1,p.getType());
    }

    @Test
    void getValue() {
        Pokemon p = createPokemon();
        assertEquals(20,p.getValue());
    }

}