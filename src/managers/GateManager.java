package managers;

import datastructures.CustomQueue;
import datastructures.CustomStack;
import models.Vehicle;
import models.ParkingSlot;
import java.util.EmptyStackException;

/**
 * Coordinates the gate flow: arrival queue, slot assignment, exit cleanup,
 * and undo. Talks to all four storage managers so that one user action
 * (Process Entry, Process Exit, Undo) keeps every data structure in sync.
 *
 * @Author ChengYang
 * UPDATED (Sprint 1 fixes):
 *   - Now also takes SearchManager and HashMapManager so storage is unified.
 *   - processNextArrival() registers in all four stores.
 *   - processExit() takes only the Vehicle (looks up the slot via
 *     vehicle.getAssignedSlot()) and removes from all four stores.
 *   - undoLastAction() for EXIT now correctly reclaims the slot from the
 *     available heap and re-registers in BST + HashMap, not just the
 *     linked list.
 *   - Common cleanup logic extracted into private helpers to avoid drift.
 */
public class GateManager {
    private CustomQueue<Vehicle> waitingQueue;
    private CustomStack<Action> historyStack;

    private final SlotManager slotManager;
    private final RecordManager recordManager;
    private final SearchManager searchManager;
    private final HashMapManager hashMapManager;

    /**
     * Records one reversible operation. Carries enough state that undo can
     * fully reverse the operation without re-querying any other manager.
     */
    private static class Action {
        final String type;          // "ENTER" or "EXIT"
        final Vehicle vehicle;
        final ParkingSlot slot;

        Action(String type, Vehicle vehicle, ParkingSlot slot) {
            this.type = type;
            this.vehicle = vehicle;
            this.slot = slot;
        }
    }

    public GateManager(SlotManager slotManager,
                       RecordManager recordManager,
                       SearchManager searchManager,
                       HashMapManager hashMapManager) {
        this.waitingQueue   = new CustomQueue<>();
        this.historyStack   = new CustomStack<>();
        this.slotManager    = slotManager;
        this.recordManager  = recordManager;
        this.searchManager  = searchManager;
        this.hashMapManager = hashMapManager;
    }

    // 1. Vehicle arrival → enter the FIFO waiting queue
    public void addVehicleToQueue(Vehicle v) {
        waitingQueue.enqueue(v);
        System.out.println("Vehicle[" + v.getLicensePlate() + "] has entered the waiting queue.");
    }

    /**
     * 2. Pull the next vehicle off the queue, assign it a slot, and register
     *    it in every storage structure. Also pushes an ENTER action onto the
     *    history stack so it can be undone.
     */
    public Vehicle processNextArrival() {
        if (waitingQueue.isEmpty()) {
            System.out.println("No vehicles in the waiting queue.");
            return null;
        }
        if (!slotManager.hasAvailableSlots()) {
            System.out.println("Parking is full! Vehicle must wait.");
            return null;
        }

        Vehicle nextVehicle  = waitingQueue.dequeue();
        ParkingSlot assigned = slotManager.assignBestSlot();
        nextVehicle.setAssignedSlot(assigned);   // NEW: link vehicle ↔ slot

        registerInAllStores(nextVehicle);

        historyStack.push(new Action("ENTER", nextVehicle, assigned));
        System.out.println("Processed arrival for: Vehicle[" + nextVehicle.getLicensePlate() + "]");
        return nextVehicle;
    }

    /**
     * 3. Process a vehicle leaving the lot. Releases the slot, removes the
     *    vehicle from every storage structure, and pushes an EXIT action.
     *
     *    The slot is found via vehicle.getAssignedSlot() — no need for the
     *    caller to look it up separately.
     */
    public void processExit(Vehicle v) {
        if (v == null) {
            System.out.println("Cannot process exit: vehicle is null.");
            return;
        }

        ParkingSlot slot = v.getAssignedSlot();
        if (slot != null) {
            slotManager.releaseSlot(slot);
        } else {
            System.out.println("Warning: vehicle " + v.getLicensePlate() + " had no slot recorded.");
        }

        forgetFromAllStores(v.getLicensePlate());

        historyStack.push(new Action("EXIT", v, slot));
        System.out.println("Processed exit for: Vehicle[" + v.getLicensePlate() + "]");
    }

    /**
     * 4. Undo the most recent action. Symmetric reversal:
     *      ENTER → forget the vehicle and release the slot
     *      EXIT  → re-register the vehicle and reclaim the slot
     */
    public void undoLastAction() {
        try {
            Action lastAction = historyStack.pop();

            if (lastAction.type.equals("ENTER")) {
                System.out.println("Undoing ENTER for: Vehicle[" + lastAction.vehicle.getLicensePlate() + "]");
                if (lastAction.slot != null) {
                    slotManager.releaseSlot(lastAction.slot);
                }
                forgetFromAllStores(lastAction.vehicle.getLicensePlate());
                lastAction.vehicle.setAssignedSlot(null);
                System.out.println("Undo: vehicle removed from all stores; slot returned to available heap.");

            } else if (lastAction.type.equals("EXIT")) {
                System.out.println("Undoing EXIT for: Vehicle[" + lastAction.vehicle.getLicensePlate() + "]");

                // Pull the slot back OUT of the available heap and re-link it to the vehicle
                if (lastAction.slot != null) {
                    slotManager.reclaimSlot(lastAction.slot);
                    lastAction.vehicle.setAssignedSlot(lastAction.slot);
                }

                // Re-register vehicle in every store
                registerInAllStores(lastAction.vehicle);
                System.out.println("Undo: vehicle re-registered; slot reclaimed and marked OCCUPIED.");
            }
        } catch (EmptyStackException e) {
            System.out.println("No actions to undo! Stack is empty.");
        }
    }

    // ============== private helpers ==============

    /** Add the vehicle to LinkedList, BST, and HashMap in one place. */
    private void registerInAllStores(Vehicle v) {
        recordManager.addRecord(v);
        searchManager.addVehicleRecord(v);
        hashMapManager.register(v);
    }

    /** Remove the vehicle from LinkedList, BST, and HashMap in one place. */
    private void forgetFromAllStores(String licensePlate) {
        recordManager.removeRecord(licensePlate);
        searchManager.removeVehicle(licensePlate);
        hashMapManager.unregister(licensePlate);
    }
}