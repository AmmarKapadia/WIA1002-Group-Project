
import Manager.SlotManager;
import models.ParkingSlot;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Hp
 */
public class Main {

    public static void main(String[] args) {
        SlotManager sm = new SlotManager();
        sm.loadSlot(new ParkingSlot[]{
            new ParkingSlot("A3", 30), new ParkingSlot("A1", 10),
            new ParkingSlot("B2", 50), new ParkingSlot("A2", 20),
            new ParkingSlot("B1", 40)});

// Should print in order: A1(10), A2(20), A3(30), B1(40), B2(50)
        sm.assignBestSlot();
        sm.assignBestSlot();
        sm.assignBestSlot();
        sm.assignBestSlot();
        sm.assignBestSlot();
    }
}
