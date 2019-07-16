package uk.ac.ncl.cemdit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.cemdit.model.provenancegraph.*;

import static org.junit.jupiter.api.Assertions.*;

class StasisPodTest {

    StasisPod stasisPod = new StasisPod();
    Entity entity1 = new Entity("uo:e1");
    Entity entity2 = new Entity("uo:e2");
    Entity entity3 = new Entity("uo:e3");
    Entity entity4 = new Entity("uo:e4");
    Entity entity5 = new Entity("uo:e5");
    Activity activity1 = new Activity("uo:a1");
    Activity activity2 = new Activity("uo:a2");
    Activity activity3 = new Activity("uo:a3");
    Activity activity4 = new Activity("uo:a4");
    Activity activity5 = new Activity("uo:a5");
    Used relation1 = new Used(activity1, entity1);
    WasGeneratedBy relation2 = new WasGeneratedBy(entity2, activity1);
    Used relation3 = new Used(activity3, entity2);
    WasGeneratedBy relation4 = new WasGeneratedBy(entity1, activity1);
    Used relation5 = new Used(activity2, entity3);
    WasGeneratedBy relation6 = new WasGeneratedBy(entity4, activity3);
    WasGeneratedBy relation7 = new WasGeneratedBy(entity3, activity5);

    @BeforeEach
    void setUp() {
        stasisPod.put(entity1);
        stasisPod.put(entity2);
        stasisPod.put(activity1);
        stasisPod.put(activity2);
        stasisPod.put(activity3);
        stasisPod.put(relation1);
        stasisPod.put(relation2);
        stasisPod.put(relation3);
        stasisPod.put(relation4);
        stasisPod.put(relation5);
    }

    @Test
    void isInPod() {
        assertTrue(stasisPod.isInPod(relation5));
        assertTrue(stasisPod.isInPod(relation1));
    }

    @Test
    void IsInPod1() {
        assertFalse(stasisPod.isInPod(activity5));
        assertFalse(stasisPod.isInPod(entity3));
        assertTrue(stasisPod.isInPod(entity1));
    }

    @Test
    void isOutreach() {
        assertTrue(stasisPod.isOutreach(relation5));
        assertFalse(stasisPod.isOutreach(relation1));
    }
}