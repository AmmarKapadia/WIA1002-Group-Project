package managers;

import datastructures.CustomQueue;
import models.Vehicle;
import models.ParkingSlot;
import java.util.EmptyStackException;

/**
 * Coordinates the gate flow: arrival queue, slot assignment,
 * exit cleanup, history management and undo.
 *
 * Sprint 2 Version:
 * - Uses HistoryManager instead of direct CustomStack
 * - Supports multi-step undo
 * - Supports action history printing
 *
 * @Author ChengYang
 */
public class GateManager {

    private CustomQueue<Vehicle> waitingQueue;

    // Sprint 2
    private HistoryManager historyManager;

    private final SlotManager slotManager;
    private final RecordManager recordManager;
    private final SearchManager searchManager;
    private final HashMapManager hashMapManager;

    // Optional stats hook
    private StatsManager statsManager;

    /** Wires the StatsManager */
    public void setStatsManager(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    public GateManager(SlotManager slotManager,
                       RecordManager recordManager,
                       SearchManager searchManager,
                       HashMapManager hashMapManager) {

        this.waitingQueue = new CustomQueue<>();

        // Sprint 2
        this.historyManager = new HistoryManager();

        this.slotManager = slotManager;
        this.recordManager = recordManager;
        this.searchManager = searchManager;
        this.hashMapManager = hashMapManager;
    }

    // =========================================================
    // Vehicle enters waiting queue
    // =========================================================

    public void addVehicleToQueue(Vehicle v) {

        waitingQueue.enqueue(v);

        System.out.println("Vehicle[" + v.getLicensePlate()
                + "] has entered the waiting queue.");
    }

    // =========================================================
    // Process next vehicle arrival
    // =========================================================

    public Vehicle processNextArrival() {

        if (waitingQueue.isEmpty()) {
            System.out.println("No vehicles in the waiting queue.");
            return null;
        }

        if (!slotManager.hasAvailableSlots()) {
            System.out.println("Parking is full! Vehicle must wait.");
            return null;
        }

        Vehicle nextVehicle = waitingQueue.dequeue();

        ParkingSlot assigned = slotManager.assignBestSlot();

        nextVehicle.setAssignedSlot(assigned);

        // register everywhere
        registerInAllStores(nextVehicle);

        // record history
        historyManager.recordAction(
                new Action(
                        "ENTER",
                        nextVehicle,
                        assigned,
                        "ENTER "
                                + nextVehicle.getLicensePlate()
                                + " -> Slot "
                                + assigned.getSlotID()
                )
        );

        // stats
        if (statsManager != null) {
            statsManager.recordEntry(nextVehicle);
        }

        System.out.println("Processed arrival for: Vehicle["
                + nextVehicle.getLicensePlate() + "]");

        return nextVehicle;
    }

    // =========================================================
    // Process vehicle exit
    // =========================================================

    public void processExit(Vehicle v) {

        if (v == null) {
            System.out.println("Cannot process exit: vehicle is null.");
            return;
        }

        ParkingSlot slot = v.getAssignedSlot();

        if (slot != null) {

            slotManager.releaseSlot(slot);

        } else {

            System.out.println("Warning: vehicle "
                    + v.getLicensePlate()
                    + " had no slot recorded.");
        }

        // remove everywhere
        forgetFromAllStores(v.getLicensePlate());

        // record history
        historyManager.recordAction(
                new Action(
                        "EXIT",
                        v,
                        slot,
                        "EXIT "
                                + v.getLicensePlate()
                                + " <- Slot "
                                + slot.getSlotID()
                )
        );

        // stats
        if (statsManager != null) {
            statsManager.recordExit(v);
        }

        System.out.println("Processed exit for: Vehicle["
                + v.getLicensePlate() + "]");
    }

    // =========================================================
    // Undo ONE action
    // =========================================================

    public void undoLastAction() {

        try {

            Action lastAction = historyManager.popLast();
            
            if (lastAction == null) {

    System.out.println("No actions to undo!");

    return;
}

            if (lastAction.getType().equals("ENTER")) {

                System.out.println("Undoing ENTER for: Vehicle["
                        + lastAction.getVehicle().getLicensePlate() + "]");

                if (lastAction.getSlot() != null) {

                    slotManager.releaseSlot(lastAction.getSlot());
                }

                forgetFromAllStores(
                        lastAction.getVehicle().getLicensePlate()
                );

                lastAction.getVehicle().setAssignedSlot(null);

                System.out.println("Undo successful.");

            }

            else if (lastAction.getType().equals("EXIT")) {

                System.out.println("Undoing EXIT for: Vehicle["
                        + lastAction.getVehicle().getLicensePlate() + "]");

                // reclaim slot
                if (lastAction.getSlot() != null) {

                    slotManager.reclaimSlot(lastAction.getSlot());

                    lastAction.getVehicle()
                            .setAssignedSlot(lastAction.getSlot());
                }

                // re-register vehicle
                registerInAllStores(lastAction.getVehicle());

                System.out.println("Undo successful.");
            }

        } catch (EmptyStackException e) {

            System.out.println("No actions to undo! Stack is empty.");
        }
    }

    // =========================================================
    // Undo MULTIPLE actions
    // =========================================================

    public void undoLast(int n) {

        int count = 0;

        while (count < n && historyManager.size() > 0) {

            undoLastAction();

            count++;
        }

        System.out.println("Undid "
                + count
                + " action(s) successfully.");
    }

    // =========================================================
    // Access HistoryManager
    // =========================================================

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    /** Register vehicle in all storage structures */
    private void registerInAllStores(Vehicle v) {

        recordManager.addRecord(v);

        searchManager.addVehicleRecord(v);

        hashMapManager.register(v);
    }

    /** Remove vehicle from all storage structures */
    private void forgetFromAllStores(String licensePlate) {

        recordManager.removeRecord(licensePlate);

        searchManager.removeVehicle(licensePlate);

        hashMapManager.unregister(licensePlate);
    }
}