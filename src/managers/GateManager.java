package managers;

import datastructures.CustomQueue;
import models.Vehicle;
import models.ParkingSlot;
import java.util.EmptyStackException;

import managers.HistoryManager.Action;;

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

        // record history (null-safe slot label for corrupted state)
        String exitSlotLabel = (slot != null) ? slot.getSlotID() : "(none)";
        historyManager.recordAction(
                new Action(
                        "EXIT",
                        v,
                        slot,
                        "EXIT "
                                + v.getLicensePlate()
                                + " <- Slot "
                                + exitSlotLabel
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


    public boolean undoLastAction() { 
        try {
            Action lastAction = historyManager.popLast();
            if (lastAction == null) {
                System.out.println("History is empty. Nothing to undo.");
                return false; 
            }

            String plate = lastAction.getVehicle().getLicensePlate();

            if (lastAction.getType().equals("ENTER")) {
                System.out.println("Undoing ENTER for: " + plate);

                if (lastAction.getSlot() != null) {
                    slotManager.releaseSlot(lastAction.getSlot());
                }

                forgetFromAllStores(plate);
                lastAction.getVehicle().setAssignedSlot(null);  // clear stale slot reference

                System.out.println("Undo Successful.");
                return true;

            } else if (lastAction.getType().equals("EXIT")) {
                System.out.println("Undoing EXIT for: " + plate);
                
                try { 
                    if (lastAction.getSlot() != null) {
                        slotManager.reclaimSlot(lastAction.getSlot());
                        lastAction.getVehicle().setAssignedSlot(lastAction.getSlot());
                    }
                    registerInAllStores(lastAction.getVehicle());
                    System.out.println("Undo Successful.");
                    return true;

                } catch (Exception e) {
                    String slotID = (lastAction.getSlot() != null)
                            ? lastAction.getSlot().getSlotID() : "(unknown)";
                    System.out.println("Partial Undo Warning: Cannot revert EXIT for [" + plate +
                                       "] because slot " + slotID +
                                       " is currently occupied.");
                    return false;
                }
            }
        } catch (EmptyStackException e) {
            System.out.println("History stack is empty.");
        }
        return false;
    }
    // =========================================================
    // Undo MULTIPLE actions
    // =========================================================

   public void undoLast(int n) {
    int attempted = 0;
    int successful = 0;

   
    while (attempted < n && historyManager.size() > 0) {
        if (undoLastAction()) { 
            successful++;
        }
        attempted++;
    }

    System.out.println("Multi-step Undo Summary: Successfully undid " + 
                       successful + "/" + attempted + " requested actions.");
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
