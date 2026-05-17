package managers;

import datastructures.CustomStack;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryManager {

    private CustomStack<Action> historyStack;

    // 自己记录数量
    private int count;

    public HistoryManager() {

        historyStack = new CustomStack<>();

        count = 0;
    }

    /**
     * Push new action into history
     */
    public void recordAction(Action action) {

        historyStack.push(action);

        count++;
    }

    /**
     * Pop latest action
     */
    public Action popLast() {

        if (historyStack.isEmpty()) {
            return null;
        }

        count--;

        return historyStack.pop();
    }

    /**
     * Return number of actions
     */
    public int size() {
        return count;
    }

  /**
     * Peek at the latest action without removing it
     */
    public Action peekLast() {
        if (historyStack.isEmpty()) {
            return null;
        }
        
        Action topAction = historyStack.pop();
        
        historyStack.push(topAction);
        
        // 3. 返回我们刚才看到的那条记录
        return topAction;
    }
    /**
     * Print latest n actions
     */
    public void printHistory(int n) {

        if (historyStack.isEmpty()) {

            System.out.println("No history found.");

            return;
        }

        System.out.println("\n=== Recent History ===");

        CustomStack<Action> tempStack = new CustomStack<>();

        int printed = 0;

        while (!historyStack.isEmpty() && printed < n) {

            Action action = historyStack.pop();

            tempStack.push(action);

            String time = new SimpleDateFormat("HH:mm:ss")
                    .format(new Date(action.getTimestamp()));

            System.out.println("[" + time + "]");

            System.out.println(action.getDescription());

            System.out.println();

            printed++;
        }

        // restore original stack
        while (!tempStack.isEmpty()) {

            historyStack.push(tempStack.pop());
        }
    }
}